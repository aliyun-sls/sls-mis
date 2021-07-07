package com.alibaba.sls.demo.apachedubbo.provider;

import com.alibaba.sls.demo.apachedubbo.inter.GreetService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

@DubboService(version = "1.0.0")
public class GreetServiceImpl implements GreetService {


    @Value("${dubbo.application.name}")
    private String serviceName;

    @Override
    public String sayHello() {
        return String.format("[%s]: HELLO", serviceName);
    }
}
