package com.alibaba.sls.demo.bave.config;

import com.alibaba.sls.demo.util.parser.NoopServicePipeLineParser;
import com.alibaba.sls.demo.util.parser.ServicePipeLineParser;
import com.alibaba.sls.demo.util.parser.toml.TomlServicePipeLineParser;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class AppAutoConfiguration {
    @Bean
    RestTemplateCustomizer useOkHttpClient(final OkHttpClient okHttpClient) {
        return new RestTemplateCustomizer() {
            public void customize(RestTemplate restTemplate) {
                restTemplate.setRequestFactory(new OkHttp3ClientHttpRequestFactory(okHttpClient));
            }
        };
    }


    @Bean
    ServicePipeLineParser servicePipeLineParser(@Value("${service.path:}") String path) throws IOException {
        if (path == null || path.length() == 0) {
            return new NoopServicePipeLineParser();
        }
        return new TomlServicePipeLineParser.Builder(path).build();
    }

    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateCustomizer restTemplateCustomizer) {
        return new RestTemplateBuilder(restTemplateCustomizer);
    }

}
