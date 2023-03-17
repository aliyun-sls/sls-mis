package works.weave.socks.billing.service;


import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.springframework.stereotype.Service;

@Service
public class ConsoleOutProcessService {

	@WithSpan
	public void process(MessageView message) {
		Span.current().setAttribute("messaging.message.id", message.getMessageId().toString());
		Span.current().setAttribute("messaging.rocketmq.client_group", "billing-service");
		Span.current().setAttribute("messaging.rocketmq.topic", message.getTopic());
	}
}
