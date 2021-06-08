package com.alibaba.sls.demo.springboot.zipkin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/hello-world")
    public String helloWorld() {
        return "hello world";
    }
}