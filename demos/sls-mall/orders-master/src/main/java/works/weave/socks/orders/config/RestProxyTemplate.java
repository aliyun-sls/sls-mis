package works.weave.socks.orders.config;

import java.net.InetSocketAddress;
import java.net.Proxy;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public final class RestProxyTemplate {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired RestTemplate restTemplate;

    @Autowired ClientHttpRequestInterceptor clientHttpRequestInterceptor;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(clientHttpRequestInterceptor);
        return restTemplate;
    }

    @Value("${proxy.host:}")
    private String host;

    @Value("${proxy.port:}")
    private String port;

    @PostConstruct
    public void init() {
        if (host.isEmpty() || port.isEmpty()) {
            return;
        }
        int portNr = -1;
        try {
            portNr = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            logger.error("Unable to parse the proxy port number");
        }
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        InetSocketAddress address = new InetSocketAddress(host, portNr);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
        factory.setProxy(proxy);

        restTemplate.setRequestFactory(factory);
        restTemplate.getInterceptors().add(clientHttpRequestInterceptor);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
