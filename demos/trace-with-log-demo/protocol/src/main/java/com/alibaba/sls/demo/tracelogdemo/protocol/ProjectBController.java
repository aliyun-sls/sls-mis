package com.alibaba.sls.demo.tracelogdemo.protocol;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public interface ProjectBController {
    @RequestMapping(method = RequestMethod.GET, value = "/createOrder")
    <T> Request<T> createOrder(@RequestParam("id") int id, @RequestParam("name") String name);
}
