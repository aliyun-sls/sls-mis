package com.alibaba.sls.demo.tracelogdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProjectA_Application {

    public static void main(String[] args) {
        SpringApplication.run(ProjectA_Application.class, args);
    }
}
