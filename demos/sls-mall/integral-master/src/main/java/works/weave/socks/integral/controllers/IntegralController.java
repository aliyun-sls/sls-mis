package works.weave.socks.integral.controllers;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import works.weave.socks.integral.dao.IntegralRecordDao;
import works.weave.socks.integral.entities.IntegralRecord;
import works.weave.socks.integral.entities.UsableIntegral;
import works.weave.socks.integral.management.IntegralManagement;

import java.util.Date;
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
        LOG.info("add[] integralRecord:{}", integralRecord);
        boolean b;
        try {
            UsableIntegral usableIntegral = new UsableIntegral(integralRecord);
             b = integralManagement.addIntegral(integralRecord, usableIntegral);
        } catch (Exception e) {
            LOG.error("IntegralController[] add fail {}", Throwables.getStackTraceAsString(e));
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @RequestMapping(value = "/intergralSum", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Float intergralSum(@RequestParam String custId) {
        LOG.info("intergralSum[] custId:{}", custId);
        Float aFloat = recordDao.intergralSum(custId);
        return aFloat;
    }

    @RequestMapping(value = "/usableIntergral", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<IntegralRecord> usableIntergral(@RequestParam String custId) {
        LOG.info("usableIntergral[] custId:{}", custId);
        IntegralRecord integralRecord = new IntegralRecord();

        integralRecord.setExpireTime(new Date());
        integralRecord.setOriginalId("vacaiia");
        integralRecord.setReason("ceshi");
        integralRecord.setType(1);
        integralRecord.setValue(78.3F);
        integralRecord.setUserId("likee");

        List<IntegralRecord> usableIntegrals = recordDao.usableIntergral(custId);
        return usableIntegrals;
    }


}
