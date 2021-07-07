package com.alibaba.sls.demo.dubboprovider;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.sls.demo.dubbointerface.GreetingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Service(interfaceClass = GreetingService.class)
@Component
public class GreetingServiceImpl implements GreetingService {

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public String sayHello() {
        return String.format("[%s] Hello,", applicationName);
    }
}
