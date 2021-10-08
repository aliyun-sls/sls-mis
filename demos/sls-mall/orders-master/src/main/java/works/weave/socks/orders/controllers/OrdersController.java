package works.weave.socks.orders.controllers;

import io.opentelemetry.api.trace.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import works.weave.socks.orders.config.OrdersConfigurationProperties;
import works.weave.socks.orders.entities.*;
import works.weave.socks.orders.repositories.CustomerOrderRepository;
import works.weave.socks.orders.resources.NewOrderResource;
import works.weave.socks.orders.services.AsyncGetService;
import works.weave.socks.orders.values.PaymentRequest;
import works.weave.socks.orders.values.PaymentResponse;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RepositoryRestController
public class OrdersController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrdersConfigurationProperties config;

    @Autowired
    private AsyncGetService asyncGetService;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Value(value = "${http.timeout:5}")
    private long timeout;

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/orders", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public
    @ResponseBody
    CustomerOrder newOrder(@RequestBody NewOrderResource item) {
        try {
            if (item.address == null || item.customer == null || item.card == null || item.items == null) {
                throw new InvalidOrderException("Invalid order request. Order requires customer, address, card and items.");
            }

            Future<Resource<Address>> addressFuture = asyncGetService.getResource(item.address, new TypeReferences
                    .ResourceType<Address>() {
            });
            Future<Resource<Customer>> customerFuture = asyncGetService.getResource(item.customer, new TypeReferences
                    .ResourceType<Customer>() {
            });
            Future<Resource<Card>> cardFuture = asyncGetService.getResource(item.card, new TypeReferences
                    .ResourceType<Card>() {
            });
            Future<List<Item>> itemsFuture = asyncGetService.getDataList(item.items, new
                    ParameterizedTypeReference<List<Item>>() {
                    });


            float amount = calculateTotal(itemsFuture.get(timeout, TimeUnit.SECONDS));
            // Call payment service to make sure they've paid
            PaymentRequest paymentRequest = new PaymentRequest(
                    addressFuture.get(timeout, TimeUnit.SECONDS).getContent(),
                    cardFuture.get(timeout, TimeUnit.SECONDS).getContent(),
                    customerFuture.get(timeout, TimeUnit.SECONDS).getContent(),
                    amount);
            LOG.info("创建订单：调用[user]后台服务-检验用户地址是否存在. 后端返回：{}", paymentRequest.getAddress());
            LOG.info("创建订单：调用[user]后台服务-检验用户银行卡是否存在. 后端返回：{}", paymentRequest.getCard());
            LOG.info("创建订单：调用[user]后台服务-检验用户是否是否存在. 后端返回：{}", paymentRequest.getCustomer());
            String customerId = parseId(customerFuture.get(timeout, TimeUnit.SECONDS).getId().getHref());

            CustomerOrder order = new CustomerOrder(
                    null,
                    customerId,
                    paymentRequest.getCustomer(),
                    paymentRequest.getAddress(),
                    paymentRequest.getCard(),
                    itemsFuture.get(timeout, TimeUnit.SECONDS),
                    Calendar.getInstance().getTime(),
                    amount);

            CustomerOrder savedOrder = customerOrderRepository.save(order);
            Span.current().setAttribute("orderId", savedOrder.getId());
            LOG.info("创建订单：创建订单成功: 客户ID: {}, 订单ID: {}", customerId, savedOrder.getId());

            if (ThreadLocalRandom.current().nextInt(1000) < 10) {
                return savedOrder;
            }

            Future<PaymentResponse> paymentFuture = asyncGetService.postResource(
                    config.getPaymentUri(),
                    paymentRequest,
                    new ParameterizedTypeReference<PaymentResponse>() {
                    });
            try {
                PaymentResponse paymentResponse = paymentFuture.get(timeout, TimeUnit.SECONDS);
                if (!paymentResponse.isAuthorised()) {
                    savedOrder.setStatus("Payment failure");
                    customerOrderRepository.save(savedOrder);
                    LOG.info("创建订单：订单ID: {} 调用[payment]后台服务-调用支付接口鉴权失败. 失败原因：{}", savedOrder.getId(), paymentResponse.getMessage());
                    throw new PaymentDeclinedException(paymentResponse.getMessage());
                }
            } catch (TimeoutException e) {
                savedOrder.setStatus("Payment failure");
                customerOrderRepository.save(savedOrder);
                LOG.info("创建订单：订单ID: {} 调用[payment]后台服务-调用支付接口超时.", savedOrder.getId());
                throw new PaymentDeclinedException("Unable to parse authorisation packet");
            } catch (Exception e) {
                savedOrder.setStatus("Payment failure");
                customerOrderRepository.save(savedOrder);
                LOG.info("创建订单：订单ID: {} 调用[payment]后台服务-调用支付接口出错.", savedOrder.getId());
                throw new PaymentDeclinedException("Payment error");
            }

            savedOrder.setStatus("payed");
            customerOrderRepository.save(savedOrder);
            LOG.info("创建订单：调用[payment]后台服务-调用支付接口成功.");
            // Ship


            Future<Shipment> shipmentFuture = asyncGetService.postResource(config.getShippingUri(), new Shipment
                    (customerId), new ParameterizedTypeReference<Shipment>() {
            });

            if (shipmentFuture.get(timeout, TimeUnit.SECONDS) == null) {
                savedOrder.setStatus("shipping failure");
                customerOrderRepository.save(savedOrder);
                LOG.warn("创建订单：调用[shipping]后台服务-调用发货接口失败.");
            } else {
                savedOrder.setStatus("shipped");
                customerOrderRepository.save(savedOrder);
                LOG.warn("创建订单：调用[shipping]后台服务-调用发货成功. Shipping: {}, ShippingName:{}",
                        shipmentFuture.get(timeout, TimeUnit.SECONDS).getId(),
                        shipmentFuture.get(timeout, TimeUnit.SECONDS).getName());
            }

            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);//设置起时间//System.out.println("111111111::::"+cal.getTime());
            cal.add(Calendar.YEAR, 1);
            IntegralRecord integralRecord = new IntegralRecord();
            integralRecord.setOriginalId(savedOrder.getId());
            integralRecord.setReason("购物");
            integralRecord.setType(0);
            integralRecord.setValue(amount);
            integralRecord.setUserId(customerId);
            integralRecord.setExpireTime(cal.getTime());
            Future<String> stringFuture = asyncGetService.postResource(config.getAntiCheatingUri(), integralRecord,
                    new ParameterizedTypeReference<String>() {
                    });
            LOG.info("创建订单：调用用户积分接口: 客户ID: {}, 订单ID: {}, Value: {}", integralRecord.getUserId(), integralRecord.getOriginalId(),
                    integralRecord.getValue());

            savedOrder.setStatus("finished");
            customerOrderRepository.save(savedOrder);
            return savedOrder;
        } catch (TimeoutException e) {
            throw new IllegalStateException("Unable to create order due to timeout from one of the services.", e);
        } catch (InterruptedException | IOException | ExecutionException e) {
            throw new IllegalStateException("Unable to create order due to unspecified IO error.", e);
        }
    }

    private String parseId(String href) {
        Pattern idPattern = Pattern.compile("[\\w-]+$");
        Matcher matcher = idPattern.matcher(href);
        if (!matcher.find()) {
            throw new IllegalStateException("Could not parse user ID from: " + href);
        }
        return matcher.group(0);
    }

//    TODO: Add link to shipping
//    @RequestMapping(method = RequestMethod.GET, value = "/orders")
//    public @ResponseBody
//    ResponseEntity<?> getOrders() {
//        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
//
//        Resources<CustomerOrder> resources = new Resources<>(customerOrders);
//
//        resources.forEach(r -> r);
//
//        resources.add(linkTo(methodOn(ShippingController.class, CustomerOrder.getShipment::ge)).withSelfRel());
//
//        // add other links as needed
//
//        return ResponseEntity.ok(resources);
//    }

    private float calculateTotal(List<Item> items) {
        float amount = 0F;
        float shipping = 4.99F;
        LOG.info("calculateTotal items:{}", items);
        amount += items.stream().mapToDouble(i -> i.getQuantity() * i.getUnitPrice()).sum();
        amount += shipping;
        return amount;
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public class PaymentDeclinedException extends IllegalStateException {
        public PaymentDeclinedException(String s) {
            super(s);
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public class InvalidOrderException extends IllegalStateException {
        public InvalidOrderException(String s) {
            super(s);
        }
    }
}
