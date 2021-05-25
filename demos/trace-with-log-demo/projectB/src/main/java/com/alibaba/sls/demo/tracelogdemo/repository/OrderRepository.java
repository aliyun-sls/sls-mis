package com.alibaba.sls.demo.tracelogdemo.repository;

import com.alibaba.sls.demo.tracelogdemo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
