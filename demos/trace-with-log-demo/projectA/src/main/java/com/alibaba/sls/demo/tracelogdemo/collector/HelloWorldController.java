package com.alibaba.sls.demo.tracelogdemo.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    private static Logger logger = LoggerFactory.getLogger(HelloWorldController.class);

    @RequestMapping("/hello-world")
    public String sayHelloWorld() {
        logger.info("hello world");
        return "Hello World";
    }
}
