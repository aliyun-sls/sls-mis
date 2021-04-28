package works.weave.socks.queuemaster.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import works.weave.socks.queuemaster.ShippingTaskHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShippingConsumerConfiguration extends RabbitMqConfiguration
{
	protected final String queueName = "shipping-task";

    @Autowired
    private ShippingTaskHandler shippingTaskHandler;

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		template.setQueue(this.queueName);
        template.setMessageConverter(jsonMessageConverter());
		return template;
	}

    @Bean
	public Queue queueName() {
		return new Queue(this.queueName, false);
	}

	@Bean
	public SimpleMessageListenerContainer listenerContainer() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueueNames(this.queueName);
		container.setMessageListener(messageListenerAdapter());

		return container;
	}

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
		MessageListenerAdapter adapter = new MessageListenerAdapter(shippingTaskHandler, jsonMessageConverter());
		adapter.setDefaultListenerMethod("onshipping");
		Map<String, String> queueOrTagToMethodName = new HashMap<>();
		queueOrTagToMethodName.put("order","onorder");
		queueOrTagToMethodName.put("shipping-task","onshipping");
		queueOrTagToMethodName.put("zhihao.miao.order","oninfo");
		adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
		return adapter;
    }
}
