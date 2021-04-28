package com.alibaba.sls.demo.bave.config;

import brave.CurrentSpanCustomizer;
import brave.SpanCustomizer;
import brave.Tracing;
import brave.baggage.BaggagePropagation;
import brave.http.HttpTracing;
import brave.okhttp3.TracingInterceptor;
import brave.propagation.B3Propagation;
import brave.propagation.CurrentTraceContext;
import brave.propagation.Propagation;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.servlet.TracingFilter;
import brave.spring.webmvc.SpanCustomizingAsyncHandlerInterceptor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;
import zipkin2.reporter.okhttp3.OkHttpSender;

import javax.servlet.Filter;


@Configuration
@Import(SpanCustomizingAsyncHandlerInterceptor.class)
public class TracingAutoConfiguration {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    CurrentTraceContext currentTraceContext() {
        return ThreadLocalCurrentTraceContext.newBuilder()
                .build();
    }

    @Bean
    Propagation.Factory propagationFactory() {
        return BaggagePropagation.newFactoryBuilder(B3Propagation.FACTORY).build();
    }


    @Bean
    Sender sender(
            @Value("${zipkin.baseUrl:http://127.0.0.1:9411}/api/v2/spans") String zipkinEndpoint) {
        return OkHttpSender.create(zipkinEndpoint);
    }

    @Bean
    AsyncZipkinSpanHandler zipkinSpanHandler(Sender sender) {
        return AsyncZipkinSpanHandler.create(sender);
    }

    /**
     * Controls aspects of tracing such as the service name that shows up in the UI
     */
    @Bean
    Tracing tracing(
            @Value("${brave.localServiceName:${spring.application.name}}") String serviceName,
            @Value("${brave.supportsJoin:true}") boolean supportsJoin,
            @Value("${brave.traceId128Bit:false}") boolean traceId128Bit,
            Propagation.Factory propagationFactory,
            CurrentTraceContext currentTraceContext,
            AsyncZipkinSpanHandler zipkinSpanHandler) {
        return Tracing.newBuilder()
                .localServiceName(serviceName)
                .supportsJoin(supportsJoin)
                .traceId128Bit(traceId128Bit)
                .propagationFactory(propagationFactory)
                .currentTraceContext(currentTraceContext)
                .addSpanHandler(zipkinSpanHandler).build();
    }

    @Bean
    SpanCustomizer spanCustomizer(Tracing tracing) {
        return CurrentSpanCustomizer.create(tracing);
    }

    @Bean
    HttpTracing httpTracing(Tracing tracing) {
        return HttpTracing.create(tracing);
    }

    @Bean
    Filter tracingFilter(HttpTracing httpTracing) {
        return TracingFilter.create(httpTracing);
    }


    @Bean
    WebMvcConfigurer tracingWebMvcConfigurer(
            final SpanCustomizingAsyncHandlerInterceptor webMvcTracingCustomizer) {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(webMvcTracingCustomizer);
            }
        };
    }


    @Bean
    OkHttpClient tracedOkHttpClient(HttpTracing httpTracing) {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(TracingInterceptor.create(httpTracing))
                .build();
    }
}
