package works.weave.socks.queuemaster.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import works.weave.socks.queuemaster.entities.HealthCheck;

import java.util.*;

@RestController
public class HealthCheckController {

//    @Autowired
//    RabbitTemplate rabbitTemplate;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, path = "/health")
    public
    @ResponseBody
    Map<String, List<HealthCheck>> getHealth() {
        Map<String, List<HealthCheck>> map = new HashMap<String, List<HealthCheck>>();
        List<HealthCheck> healthChecks = new ArrayList<HealthCheck>();
        Date dateNow = Calendar.getInstance().getTime();

        HealthCheck app = new HealthCheck("queue-master", "OK", dateNow);
        HealthCheck rabbitmq = new HealthCheck("queue-master-rabbitmq", "OK", dateNow);

//        try {
//            this.rabbitTemplate.execute(new ChannelCallback<String>() {
//                @Override
//                public String doInRabbit(Channel channel) throws Exception {
//                    Map<String, Object> serverProperties = channel.getConnection().getServerProperties();
//                    return serverProperties.get("version").toString();
//                }
//            });
//        } catch ( AmqpException e ) {
//            rabbitmq.setStatus("err");
//        }

        healthChecks.add(app);
        healthChecks.add(rabbitmq);

        map.put("health", healthChecks);
        return map;
    }
}
