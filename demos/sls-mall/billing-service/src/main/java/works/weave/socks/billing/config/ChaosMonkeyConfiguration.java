package works.weave.socks.billing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ChaosMonkeyConfiguration {

	@Autowired
	private OrderMessageListener orderMessageListener;

	@Scheduled(cron = "0 0/30 * * * *")
	public void chaosMonkey() {
		orderMessageListener.startChaos();
	}




}
