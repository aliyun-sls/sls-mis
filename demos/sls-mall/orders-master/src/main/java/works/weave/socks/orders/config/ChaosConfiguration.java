package works.weave.socks.orders.config;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ChaosConfiguration {

    @Value("CLIENT_MAX_SLEEP_TIME_US")
    private int clientMaxSleepTimeUS;

    @Value("CLIENT_MIN_SLEEP_TIME_US")
    private int clientMinSleepTimeUS;

    @Value("SLOW_P")
    private int slowP;

    @Value("SERVER_MIN_SLEEP_TIME_US")
    private int serverMinSleepTimeUS;

    @Value("THROW_EXCEPTION_P")
    private int throwExceptionP;

    @Value("SERVER_MAX_SLEEP_TIME_US")
    private int serverMaxSleepTimeUS;

    @Pointcut("execution (* works.weave.socks.orders.controllers.OrdersController.newOrder(..))")
    public void orderController() {
    }

    @Around("orderController()")
    public void aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        if (ThreadLocalRandom.current().nextInt(100) > 1 - slowP) {
            int sleepTime = ThreadLocalRandom.current().nextInt(serverMinSleepTimeUS, serverMaxSleepTimeUS);
            LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(sleepTime));
        }

        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (ThreadLocalRandom.current().nextInt(100) > 1 - throwExceptionP) {
                throw new IllegalStateException("Mock Exception");
            }
        }
    }

    @Bean
    public ClientHttpRequestInterceptor clientHttpRequestInterceptor() {
        return new ClientHttpRequestInterceptor() {
            @Override public ClientHttpResponse intercept(HttpRequest request, byte[] bytes,
                ClientHttpRequestExecution execution) throws IOException {
                if (ThreadLocalRandom.current().nextInt(100) > 1 - slowP) {
                    int sleepTime = ThreadLocalRandom.current().nextInt(clientMinSleepTimeUS, clientMaxSleepTimeUS);
                    LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(sleepTime));
                }
                try {
                    return execution.execute(request, bytes);
                } catch (Throwable e) {
                    throw e;
                } finally {
                    if (ThreadLocalRandom.current().nextInt(100) > 1 - throwExceptionP) {
                        throw new IllegalStateException("Mock Exception");
                    }
                }
            }
        };
    }
}
