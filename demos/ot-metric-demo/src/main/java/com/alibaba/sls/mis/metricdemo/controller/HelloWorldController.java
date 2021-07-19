package com.alibaba.sls.mis.metricdemo.controller;

import io.opentelemetry.api.metrics.GlobalMeterProvider;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    private Meter meter = GlobalMeterProvider.getMeter("test.test");
    private LongCounter longCounter = meter.longCounterBuilder("test").build();

    @RequestMapping("/sayHello")
    public String sayHello() {
        longCounter.add(1);
        return "Hello World";
    }
}
