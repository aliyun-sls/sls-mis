package works.weave.socks.billing.config;

import ch.qos.logback.core.util.TimeUtil;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.MessageListener;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import works.weave.socks.billing.service.ChaosProcessorService;
import works.weave.socks.billing.service.ConsoleOutProcessService;

@Component
public class OrderMessageListener implements MessageListener {

	private AtomicBoolean chaosMonkey = new AtomicBoolean(false);

	private AtomicLong startTime = new AtomicLong(System.currentTimeMillis());

	@Autowired
	private ChaosProcessorService chaosProcessorService;

	@Autowired
	private ConsoleOutProcessService consoleOutProcessService;

	@Override
	@WithSpan
	public ConsumeResult consume(MessageView messageView) {
		if (chaosMonkey.get() && System.currentTimeMillis() - startTime.get() > TimeUnit.MINUTES.toMillis(5)) {
			chaosProcessorService.process();
		}

		consoleOutProcessService.process(messageView);
		return ConsumeResult.SUCCESS;
	}

	public void startChaos() {
		startTime.set(System.currentTimeMillis());
		chaosMonkey.set(true);
	}
}
