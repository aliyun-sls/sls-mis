package com.alibaba.sls.demo.tracelogdemo.collector;

import com.alibaba.sls.demo.tracelogdemo.exception.BusinessException;
import com.alibaba.sls.demo.tracelogdemo.protocol.Request;
import com.alibaba.sls.demo.tracelogdemo.service.ProjectBServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallServiceController {

    private static Logger logger = LoggerFactory.getLogger(CallServiceController.class);

    @Autowired
    private ProjectBServices projectBServices;

    @RequestMapping("/createOrder")
    public Request createOrder(@RequestParam("id") Integer id, @RequestParam("name") String name) {
        if (id == null || id == 0 || name == null || name.length() == 0) {
            logger.info("参数不正确, 客户ID:{}, 商品信息: {}", id, name);
            throw new RuntimeException();
        }
        logger.info("创建订单：用户ID:{}, 商品信息：{}", id, name);
        Request request = projectBServices.createOrder(id, name);
        if (!request.isSuccess()) {
            throw new BusinessException("创建订单失败:" + request.getMessage());
        }
        return Request.ofSuccess("创建订单成功");
    }
}