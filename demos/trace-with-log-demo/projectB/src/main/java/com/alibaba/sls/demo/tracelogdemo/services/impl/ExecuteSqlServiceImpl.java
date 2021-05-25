package com.alibaba.sls.demo.tracelogdemo.services.impl;

import com.alibaba.sls.demo.tracelogdemo.entity.Customer;
import com.alibaba.sls.demo.tracelogdemo.entity.Order;
import com.alibaba.sls.demo.tracelogdemo.exception.CustomerNotExistException;
import com.alibaba.sls.demo.tracelogdemo.repository.CustomerRepository;
import com.alibaba.sls.demo.tracelogdemo.repository.OrderRepository;
import com.alibaba.sls.demo.tracelogdemo.services.ExecuteSQLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class ExecuteSqlServiceImpl implements ExecuteSQLService {

    private Logger logger = LoggerFactory.getLogger(ExecuteSqlServiceImpl.class);

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
