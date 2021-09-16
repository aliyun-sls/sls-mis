package com.aliyun.sls.demo.tracewithlog.backend.repository;

import com.aliyun.sls.demo.tracewithlog.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
