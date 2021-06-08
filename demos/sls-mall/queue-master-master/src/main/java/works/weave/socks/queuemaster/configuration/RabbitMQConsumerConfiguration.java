package works.weave.socks.queuemaster.configuration;


import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConsumerConfiguration {

    @Value("${spring.rabbitmq.host:rabbitmq}")
    private String host;

    @Value("${spring.rabbitmq.port:5672}")
    private int port;


    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        return factory;
    }

    @Bean
    public ShipmentConsumer shipmentConsumer() {
        return new ShipmentConsumerImpl();
    }


}
