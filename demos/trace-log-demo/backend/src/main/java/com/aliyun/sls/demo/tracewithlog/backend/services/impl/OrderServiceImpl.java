package com.aliyun.sls.demo.tracewithlog.backend.services.impl;

import com.aliyun.sls.demo.tracewithlog.backend.entity.Customer;
import com.aliyun.sls.demo.tracewithlog.backend.entity.Order;
import com.aliyun.sls.demo.tracewithlog.backend.exception.CustomerNotExistException;
import com.aliyun.sls.demo.tracewithlog.backend.repository.CustomerRepository;
import com.aliyun.sls.demo.tracewithlog.backend.repository.OrderRepository;
import com.aliyun.sls.demo.tracewithlog.backend.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderServiceImpl implements OrderService {

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;


    @Override
    public Order createOrder(int id, String name) {
        logger.info("查找客户信息, 客户ID:{}", id);
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotExistException(id));
        logger.info("创建订单, 购买商品名字：{}", name);
        Order order = orderRepository.save(new Order(ThreadLocalRandom.current().nextInt(), name));
        logger.info("创建订单，订单ID{}", order.getId());
        return order;
    }
}
