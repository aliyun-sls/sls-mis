package com.alibaba.sls.demo.dubboconsumer;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubboConfiguration
@SpringBootApplication
public class DubboConsumerDemo {

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerDemo.class);
    }
}
