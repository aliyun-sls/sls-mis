package works.weave.socks.billing.service;

import java.util.Collections;
import javax.annotation.PostConstruct;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.SessionCredentialsProvider;
import org.apache.rocketmq.client.apis.StaticSessionCredentialsProvider;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.apache.rocketmq.client.apis.consumer.SimpleConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import works.weave.socks.billing.config.OrderMessageListener;

@Service
public class RocketMQService {

	@Autowired
	private ClientServiceProvider provider;

	@Value("${ROCKETMQ_ACCESSKEY:}")
	private String accessKey;

	@Value("${ROCKETMQ_SECRETKEY:}")
	private String secretKey;

	@Value("${ROCKETMQ_ENDPOINTS:}")
	private String endpoints;

	@Value("${ROCKETMQ_ORDER_TOPIC:}")
	private String topic;

	@Value("${ROCKETMQ_CONSUMER_GROUP:billing-service}")
	private String consumerGroup;

	@Autowired
	private OrderMessageListener messageListener;

	@PostConstruct
	public void init() throws ClientException {
		SessionCredentialsProvider sessionCredentialsProvider =
			new StaticSessionCredentialsProvider(accessKey, secretKey);

		ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
			.setEndpoints(endpoints)
			.setCredentialProvider(sessionCredentialsProvider)
			.build();
		FilterExpression filterExpression = new FilterExpression("*", FilterExpressionType.TAG);

		PushConsumer pushConsumer = provider.newPushConsumerBuilder()
			.setClientConfiguration(clientConfiguration)
			.setConsumerGroup(consumerGroup)
			.setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
			.setMessageListener(messageListener)
			.build();
	}
}
