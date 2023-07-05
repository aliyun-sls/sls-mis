package com.alibaba.sls.demo;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class KubeApplicationVets {

	public static void main(String[] args) {
		log.debug("In Testing Logs KubeApplicationVets Service. debug");
		log.trace("In Testing Logs KubeApplicationVets Service. trace");
		log.info("In Testing Logs KubeApplicationVets Service. info");
		SpringApplication.run(KubeApplicationVets.class, args);
	}

}
