package com.aliyun.sls.demo.tracewithlog.backend.services;

import com.aliyun.sls.demo.tracewithlog.backend.entity.Order;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    Order createOrder(int id, String name);
}
