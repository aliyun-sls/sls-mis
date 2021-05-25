package com.alibaba.sls.demo.tracelogdemo.collector;

import com.alibaba.sls.demo.tracelogdemo.entity.Order;
import com.alibaba.sls.demo.tracelogdemo.exception.CustomerNotExistException;
import com.alibaba.sls.demo.tracelogdemo.protocol.ProjectBController;
import com.alibaba.sls.demo.tracelogdemo.protocol.Request;
import com.alibaba.sls.demo.tracelogdemo.services.ExecuteSQLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallServiceController implements ProjectBController {

    private static final Logger logger = LoggerFactory.getLogger(CallServiceController.class);

    @Autowired
    private ExecuteSQLService executeSQLService;

    @RequestMapping(method = RequestMethod.GET, value = "/createOrder")
    public Request<Order> createOrder(@RequestParam("id") int id, @RequestParam("name") String name) {
        try {
            logger.info("创建订单参数为: {}, {}", id, name);
            return Request.ofSuccess(executeSQLService.createOrder(id, name));
        } catch (CustomerNotExistException e) {
            logger.error("用户ID[{}]不存在", id, e);
            return Request.ofFailure("用户ID[" + id + "]不存在");
        }
    }
}
