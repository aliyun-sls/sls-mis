package com.alibaba.sls.demo.tracelogdemo.services.impl;

import com.alibaba.sls.demo.tracelogdemo.entity.Customer;
import com.alibaba.sls.demo.tracelogdemo.repository.CustomerRepository;
import com.alibaba.sls.demo.tracelogdemo.services.ExecuteSQLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ExecuteSqlServiceImpl implements ExecuteSQLService {

    private Logger logger = LoggerFactory.getLogger(ExecuteSqlServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public String errorTrace(String name) throws Exception {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 1500));
            throw new RuntimeException();
        } catch (RuntimeException e) {
            logger.error("EVENT_TYPE=EVENT_CONNECTION_POOL_ERROR {}", "Too Many Connections for Mysql[localhost:3306/test]", e);
            throw e;
        }
    }

    @Override
    public List<Customer> slowTrace(String name) throws InterruptedException {
        logger.info("CallService ExecuteSqlServiceImpl.slowTrace with parameter: {}", name);
        Thread.sleep(500);
        try {
            return customerRepository.findAll();
        } finally {
            Thread.sleep(500);
        }
    }


    @Override
    public List<Customer> normalTrace(String name) {
        logger.info("CallService ExecuteSqlServiceImpl.normalTrace with parameter: {}", name);
        return customerRepository.findAll();
    }
}
