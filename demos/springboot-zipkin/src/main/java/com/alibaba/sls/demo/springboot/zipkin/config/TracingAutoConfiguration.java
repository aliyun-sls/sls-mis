package com.alibaba.sls.demo.springboot.zipkin.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.zipkin2.ZipkinRestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class TracingAutoConfiguration {


    @Bean("interceptors")
    public List<ClientHttpRequestInterceptor> interceptors(@Value("${PROJECT}") String project, @Value("${INSTANCE}") String instance,
                                                           @Value("${ACCESS_KEY_ID}") String akId, @Value("${ACCESS_SECRET}") String akSecret) {
        return Stream.of((ClientHttpRequestInterceptor) (httpRequest, bytes, clientHttpRequestExecution) -> {
            httpRequest.getHeaders().add("x-sls-otel-project", project);
            httpRequest.getHeaders().add("x-sls-otel-instance-id", instance);
            httpRequest.getHeaders().add("x-sls-otel-ak-id", akId);
            httpRequest.getHeaders().add("x-sls-otel-ak-secret", akSecret);
            return clientHttpRequestExecution.execute(httpRequest, bytes);
        }).collect(Collectors.toList());
    }

    @Bean("zipkinRequestFactory")
    public ClientHttpRequestFactory httpRequestFactory() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(100000);
        simpleClientHttpRequestFactory.setReadTimeout(10000);
        return simpleClientHttpRequestFactory;
    }

    @Bean
    public ZipkinRestTemplateCustomizer zipkinRestTemplateCustomizer(@Qualifier("interceptors") List<ClientHttpRequestInterceptor> interceptors,
                                                                     @Qualifier("zipkinRequestFactory") ClientHttpRequestFactory httpRequestFactory) {
        return new ZipkinRestTemplateCustomizer() {
            @Override
            public RestTemplate customizeTemplate(RestTemplate restTemplate) {
                restTemplate.setInterceptors(interceptors);
                restTemplate.setRequestFactory(httpRequestFactory);
                return restTemplate;
            }
        };
    }
}
