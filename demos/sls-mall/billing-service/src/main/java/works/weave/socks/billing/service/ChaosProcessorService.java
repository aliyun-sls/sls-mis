package works.weave.socks.billing.service;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class ChaosProcessorService {

	private static final long MIN_SLEEP_TIME = TimeUnit.SECONDS.toMillis(1);
	private static final long MAX_SLEEP_TIME = TimeUnit.SECONDS.toMillis(5);

	@WithSpan
	public void process() {
		try {
			Thread.sleep((ThreadLocalRandom.current().nextInt(0, 1) * (MAX_SLEEP_TIME - MIN_SLEEP_TIME)
				+ MIN_SLEEP_TIME));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
