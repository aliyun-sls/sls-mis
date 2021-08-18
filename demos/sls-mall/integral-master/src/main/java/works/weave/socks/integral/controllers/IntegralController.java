package works.weave.socks.integral.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import works.weave.socks.integral.dao.IntegralRecordDao;
import works.weave.socks.integral.entities.IntegralRecord;
import works.weave.socks.integral.entities.UsableIntegral;
import works.weave.socks.integral.management.IntegralManagement;

import java.util.List;

@RestController
@RequestMapping
public class IntegralController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private IntegralRecordDao recordDao;
    @Autowired
    private IntegralManagement integralManagement;

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Boolean add(@RequestBody IntegralRecord integralRecord) {
        LOG.info("收到添加积分请求: 用户ID：{}, 用户积分：{}，原因: {}", integralRecord.getUserId(), integralRecord.getValue(), integralRecord.getReason());
        try {
            UsableIntegral usableIntegral = new UsableIntegral(integralRecord);
            Boolean result = integralManagement.addIntegral(integralRecord, usableIntegral);
            LOG.info("添加积分成功: 用户ID: {}", integralRecord.getUserId());
            return result;
        } catch (Exception e) {
            LOG.error("添加积分失败", e);
            return Boolean.FALSE;
        }
    }

    @RequestMapping(value = "/intergralSum", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Float intergralSum(@RequestParam String custId) {
        Float aFloat = recordDao.intergralSum(custId);
        LOG.info("查询用户总积分: 用户ID：{}, 积分: {}", custId, aFloat);
        return aFloat;
    }

    @RequestMapping(value = "/usableIntergral", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<IntegralRecord> usableIntergral(@RequestParam String custId) {
        List<IntegralRecord> usableIntegrals = recordDao.usableIntergral(custId);
        LOG.info("查询用户积分明细: 用户ID：{}, 积分明细条数: {}", custId, usableIntegrals.size());
        return usableIntegrals;
    }


}
