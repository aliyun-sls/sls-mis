package com.alibaba.sls.demos.otelhttp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController public class HelloWorldController {

    @RequestMapping("/sayHello") public String sayHello() {
        return "Hello World";
    }
}
