package com.alibaba.sls.demos.service.impl;

import com.alibaba.sls.demos.service.DemoService;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {

	@Override
	@WithSpan
	public String capture(@SpanAttribute("test") String name) {
		return "Hello World, " + name;
	}
}
