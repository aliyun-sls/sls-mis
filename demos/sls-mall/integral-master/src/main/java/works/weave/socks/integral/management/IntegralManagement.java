package works.weave.socks.integral.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import works.weave.socks.integral.dao.IntegralRecordDao;
import works.weave.socks.integral.dao.ReduceIntegralDetailDao;
import works.weave.socks.integral.dao.UsableIntegralDao;
import works.weave.socks.integral.entities.IntegralRecord;
import works.weave.socks.integral.entities.ReduceIntegralDetail;
import works.weave.socks.integral.entities.UsableIntegral;

import java.util.List;

@Component
public class IntegralManagement {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private IntegralRecordDao integralRecordDao;
    @Autowired
    private UsableIntegralDao usableIntegralDao;
    @Autowired
    private ReduceIntegralDetailDao reduceIntegralDetailDao;

    @Transactional(rollbackFor = Exception.class)
    public boolean addIntegral(IntegralRecord integralRecord, UsableIntegral usableIntegral) {
        integralRecordDao.save(integralRecord);
        usableIntegral.setRecordId(integralRecord.getId());
        usableIntegralDao.save(usableIntegral);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean batchIntegral(List<UsableIntegral> usableIntegrals, List<ReduceIntegralDetail> reduceIntegralDetails) {
        usableIntegralDao.updateExpire(usableIntegrals);
        int saveNum = reduceIntegralDetailDao.batchAdd(reduceIntegralDetails);
        LOG.info("batchIntegral[]  saveNum:{}", saveNum);
        return true;
    }
}
