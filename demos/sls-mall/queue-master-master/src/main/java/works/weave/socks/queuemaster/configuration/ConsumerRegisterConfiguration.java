package works.weave.socks.queuemaster.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import works.weave.socks.shipping.entities.Shipment;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
public class ConsumerRegisterConfiguration {

    @Value("${queueName:shipping-task}")
    private String queueName;

    @Autowired
    private ConnectionFactory factory;
    @Autowired
    private ShipmentConsumer consumer;

    @PostConstruct
    public void initConsumer() throws IOException, TimeoutException {
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.basicConsume(queueName, true, new DeliverConsumer() {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String json = new String(body, StandardCharsets.UTF_8);
                ObjectMapper objectMapper = new ObjectMapper();
                Shipment shipment = objectMapper.readValue(json, Shipment.class);
                consumer.OnMessage(shipment);
            }
        });
    }
}
