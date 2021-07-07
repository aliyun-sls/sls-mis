package com.alibaba.sls.demo.apachedubbo.provider;

import com.alibaba.sls.demo.apachedubbo.inter.GreetingService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

@DubboService(version = "1.0.0")
public class GreetingServiceImpl implements GreetingService {


    @Value("${dubbo.application.name}")
    private String serviceName;

    @Override
    public String sayHello() {
        return String.format("[%s]: HELLO", serviceName);
    }
}
