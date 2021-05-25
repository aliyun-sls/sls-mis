package com.alibaba.sls.demo.tracelogdemo.services;

import com.alibaba.sls.demo.tracelogdemo.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExecuteSQLService {
    String errorTrace(String name) throws Exception;

    List<Customer> slowTrace(String name) throws Exception;

    List<Customer> normalTrace(String name);
}
