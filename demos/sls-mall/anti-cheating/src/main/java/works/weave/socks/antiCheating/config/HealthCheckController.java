package works.weave.socks.antiCheating.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import works.weave.socks.antiCheating.config.HealthCheck;

import java.util.*;

@RestController
public class HealthCheckController {

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, path = "/health")
    public
    @ResponseBody
    Map<String, List<HealthCheck>> getHealth() {
        Map<String, List<HealthCheck>> map = new HashMap<String, List<HealthCheck>>();
        List<HealthCheck> healthChecks = new ArrayList<HealthCheck>();
        Date dateNow = Calendar.getInstance().getTime();

        HealthCheck app = new HealthCheck("ant-cheating", "OK", dateNow);
        HealthCheck database = new HealthCheck("integral-db", "OK", dateNow);

        healthChecks.add(app);
        healthChecks.add(database);

        map.put("health", healthChecks);
        return map;
    }
}
