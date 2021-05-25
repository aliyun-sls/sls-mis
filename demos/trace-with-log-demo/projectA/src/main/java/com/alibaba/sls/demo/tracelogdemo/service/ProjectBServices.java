package com.alibaba.sls.demo.tracelogdemo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "projectBServices", url = "http://${projectb:projectb}:9081")
public interface ProjectBServices {
    @RequestMapping(method = RequestMethod.GET, value = "/normalTrace")
    List normalTrace(String name);

    @RequestMapping(method = RequestMethod.GET, value = "/slowTrace")
    List slowTrace(String name);

    @RequestMapping(method = RequestMethod.GET, value = "/errorTrace")
    List errorTrace(String name);
}
