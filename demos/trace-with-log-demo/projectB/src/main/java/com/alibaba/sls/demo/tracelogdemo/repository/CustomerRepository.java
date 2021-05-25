package com.alibaba.sls.demo.tracelogdemo.repository;

import com.alibaba.sls.demo.tracelogdemo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
