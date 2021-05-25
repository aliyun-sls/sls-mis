package com.alibaba.sls.demo.tracelogdemo.services;

import com.alibaba.sls.demo.tracelogdemo.entity.Order;
import org.springframework.stereotype.Service;

@Service
public interface ExecuteSQLService {
    Order createOrder(int id, String name);
}
