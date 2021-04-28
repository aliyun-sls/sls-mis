package works.weave.socks.queuemaster.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import works.weave.socks.queuemaster.entities.HealthCheck;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ITHealthCheckController {

    @Autowired
    private HealthCheckController healthCheckController;

    @Test
    public void getHealthCheck() throws Exception {
        Map<String, List<HealthCheck>> healthChecks = healthCheckController.getHealth();
        assertThat(healthChecks.get("health").size(), is(equalTo(2)));
    }
}
