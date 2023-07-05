package com.alibaba.sls.demo;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HelloService {


	@RequestMapping("/svc")
	public String hello() throws IOException {
		log.debug("In Testing Logs HelloService Service. debug");
		log.trace("In Testing Logs HelloService Service. trace");
		log.info("In Testing Logs HelloService Service. info");
		log.info("asdfasdg {}", "asdfasdf");
		return String.format("doctor-service This is hello from doctor service!!");
	}
}
