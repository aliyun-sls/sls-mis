package com.alibaba.sls.demo.ot.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrossProcessController {

    @RequestMapping("/callNodeJs")
    public String callNodeJsProject() {
        return "";
    }

    @RequestMapping("/callPHP")
    public String callPHPProject() {
        return "";
    }

    @RequestMapping("/callGo")
    public String callGoProject() {
        return "";
    }
}
