package works.weave.socks.orders.services;

import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RocketMQService {

	@Autowired
	private ClientServiceProvider provider;

	@Autowired
	private Producer producer;

	@Value("${ROCKETMQ_ORDER_TOPIC:}")
	private String topic;

	public boolean sentMessage(String messageStr) throws ClientException {
		final Message message = provider.newMessageBuilder()
			.setTopic(topic)
			.setBody(messageStr.getBytes())
			.build();

		producer.send(message);
		return true;
	}
}
