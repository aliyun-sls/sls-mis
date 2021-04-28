package com.alibaba.sls.demo.bave.controller;

import com.alibaba.sls.demo.util.parser.ServicePipeLineParser;
import com.alibaba.sls.demo.util.parser.exception.ServiceNotExistException;
import com.alibaba.sls.demo.util.parser.exception.StepNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CallServiceController {


    @Autowired
    private ServicePipeLineParser serviceLineParser;

    @Autowired
    private RestTemplate restTemplate;


    @RequestMapping("/callservice")
    public String callService(@RequestParam String serviceName, @RequestParam(required = false) String nextPath) {
        if (nextPath == null || nextPath.length() == 0) {
            return "brave-springboot-demo";
        }

        try {
            return "brave-springboot-demo" + restTemplate.getForObject(serviceLineParser.parse(serviceName, nextPath).visitUrl(), String.class);
        } catch (ServiceNotExistException | StepNotExistException e) {
            return "brave-springboot-demo";
        }
    }
}
