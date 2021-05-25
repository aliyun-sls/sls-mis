package com.alibaba.sls.demo.tracelogdemo.collector;

import com.alibaba.sls.demo.tracelogdemo.entity.Customer;
import com.alibaba.sls.demo.tracelogdemo.services.ExecuteSQLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CallServiceController {

    private static final Logger logger = LoggerFactory.getLogger(CallServiceController.class);

    @Autowired
    private ExecuteSQLService executeSQLService;

    @RequestMapping("/normalTrace")
    public List<Customer> normalTrace(String name) {
        logger.info("CallService normalTrace with Parameter:" + name);
        return executeSQLService.normalTrace(name);
    }

    @RequestMapping("/slowTrace")
    public List<Customer> slowTrace(String name) throws Exception {
        logger.info("CallService slowTrace with Parameter:" + name);
        return executeSQLService.slowTrace(name);
    }

    @RequestMapping("/errorTrace")
    public String errorTrace(String name) throws Exception {
        logger.info("CallService errorTrace with Parameter:" + name);
        return executeSQLService.errorTrace(name);
    }
}
