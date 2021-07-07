package com.alibaba.sls.demo.apachedubbo.consumer.controller;

import com.alibaba.sls.demo.apachedubbo.inter.GreetService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetController {

    @DubboReference(version = "1.0.0", url = "dubbo://127.0.0.1:47895")
    private GreetService greetService;

    @RequestMapping("/sayHello")
    public String sayHello() {
        return greetService.sayHello();
    }
}
