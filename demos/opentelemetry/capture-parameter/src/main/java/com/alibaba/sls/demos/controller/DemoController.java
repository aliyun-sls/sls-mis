package com.alibaba.sls.demos.controller;

import com.alibaba.sls.demos.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

	@Autowired
	DemoService demoService;

	@RequestMapping("/capture/{name}")
	public String capture(@PathVariable String name, @RequestParam(required = false) String captureParam,
		@RequestParam(required = false) String donotCaputureParam) {
		return demoService.capture(String.format("%s, %s", captureParam, donotCaputureParam));
	}

}
