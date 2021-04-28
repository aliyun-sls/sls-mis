package com.alibaba.sls.demo.skywalking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ServiceController {

    @Autowired
    public RestTemplate restTemplate;

    @RequestMapping("/callHelloWorld")
    public String callHelloWorld() {
        return "call service" + restTemplate.getForObject("http://localhost:8080/hello-world", String.class);
    }
}
