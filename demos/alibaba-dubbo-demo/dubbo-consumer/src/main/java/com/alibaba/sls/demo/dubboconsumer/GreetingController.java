package com.alibaba.sls.demo.dubboconsumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.sls.demo.dubbointerface.GreetingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @Reference(url = "dubbo://127.0.0.1:20880")
    private GreetingService greetingService;

    @RequestMapping("/sayHello")
    public String sayHello() {
        return greetingService.sayHello();
    }
}
