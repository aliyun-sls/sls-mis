package com.alibaba.sls.demo.tracelogdemo.service;

import com.alibaba.sls.demo.tracelogdemo.protocol.ProjectBController;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "projectBServices", url = "http://${projectb:projectb}:9081")
public interface ProjectBServices extends ProjectBController {
}
