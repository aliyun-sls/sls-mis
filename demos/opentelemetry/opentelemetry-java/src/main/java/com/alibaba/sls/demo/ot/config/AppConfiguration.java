package com.alibaba.sls.demo.ot.config;

import com.alibaba.sls.demo.util.parser.NoopServicePipeLineParser;
import com.alibaba.sls.demo.util.parser.ServicePipeLineParser;
import com.alibaba.sls.demo.util.parser.toml.TomlServicePipeLineParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class AppConfiguration {


    @Bean
    ServicePipeLineParser servicePipeLineParser(@Value("${service.path:}") String path) throws IOException {
        if (path == null || path.length() == 0) {
            return new NoopServicePipeLineParser();
        }
        return new TomlServicePipeLineParser.Builder(path).build();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
