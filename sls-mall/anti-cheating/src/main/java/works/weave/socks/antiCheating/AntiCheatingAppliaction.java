package works.weave.socks.antiCheating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AntiCheatingAppliaction {
    public static void main(String[] args) {
        SpringApplication.run(AntiCheatingAppliaction.class, args);

    }
}
