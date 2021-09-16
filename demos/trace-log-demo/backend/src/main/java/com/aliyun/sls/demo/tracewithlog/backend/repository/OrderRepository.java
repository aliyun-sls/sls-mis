package com.aliyun.sls.demo.tracewithlog.backend.repository;

import com.aliyun.sls.demo.tracewithlog.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
