package works.weave.socks.orders.config;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.SessionCredentialsProvider;
import org.apache.rocketmq.client.apis.StaticSessionCredentialsProvider;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class RocketMQConfiguration {

	@Value("${ROCKETMQ_ACCESSKEY:}")
	private String accessKey;

	@Value("${ROCKETMQ_SECRETKEY:}")
	private String secretKey;

	@Value("${ROCKETMQ_ENDPOINTS:}")
	private String endpoints;

	@Value("${ROCKETMQ_ORDER_TOPIC:}")
	private String topic;

	@Bean
	public Producer rocketMQProducer(ClientServiceProvider provider) throws ClientException {
		SessionCredentialsProvider sessionCredentialsProvider =
			new StaticSessionCredentialsProvider(accessKey, secretKey);

		ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
			.setEndpoints(endpoints)
			.setCredentialProvider(sessionCredentialsProvider)
			.build();
		return provider.newProducerBuilder()
			.setClientConfiguration(clientConfiguration)
			.setTopics(topic).build();
	}


	@Bean
	public ClientServiceProvider clientServiceProvider() {
		return ClientServiceProvider.loadService();
	}
}
