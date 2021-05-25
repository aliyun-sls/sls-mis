package com.alibaba.sls.demo.tracelogdemo.collector;

import com.alibaba.sls.demo.tracelogdemo.service.ProjectBServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class CallServiceController {

    private static Logger logger = LoggerFactory.getLogger(CallServiceController.class);

    @Autowired
    private ProjectBServices projectBServices;

    @RequestMapping("/normal-trace")
    public List forNormalTrace(@PathVariable(name = "name", required = false) String name) {
        logger.info("Call forNormalTrace Service: {}", name);
        return projectBServices.normalTrace(name);
    }

    @RequestMapping("/slow-trace/{name}")
    public List forSlowTrace(@PathVariable(name = "name", required = false) String name) throws Exception {
        logger.info("Call forSlowTrace Service: {}", name);
        Thread.sleep(ThreadLocalRandom.current().nextInt(100, 500));
        return projectBServices.slowTrace(name);
    }

    @RequestMapping("/error-trace/{name}")
    public List forErrorTrace(@PathVariable(name = "name", required = false) String name) {
        logger.info("Call forErrorTrace Service: {}", name);
        return projectBServices.errorTrace(name);
    }
}