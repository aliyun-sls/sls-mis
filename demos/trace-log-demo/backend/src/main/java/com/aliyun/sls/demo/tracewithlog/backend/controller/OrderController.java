package com.aliyun.sls.demo.tracewithlog.backend.controller;

import com.aliyun.sls.demo.tracewithlog.backend.entity.Order;
import com.aliyun.sls.demo.tracewithlog.backend.exception.CustomerNotExistException;
import com.aliyun.sls.demo.tracewithlog.backend.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET, value = "/createOrder")
    public Request createOrder(@RequestParam("id") int id, @RequestParam("name") String name) {
        try {
            logger.info("创建订单参数为: 客户ID: {}, 商品名字：{}", id, name);
            return Request.ofSuccess(orderService.createOrder(id, name));
        } catch (CustomerNotExistException e) {
            return Request.ofFailure("创建失败，客户ID不存在");
        }
    }
}
