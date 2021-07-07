package com.alibaba.sls.demo.dubboprovider;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
public class DubboProviderDemo {

    public static void main(String[] args) {
        SpringApplication.run(DubboProviderDemo.class);
    }
}
