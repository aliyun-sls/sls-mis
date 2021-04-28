package com.alibaba.sls.demo.skywalking.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfiguration {


    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
