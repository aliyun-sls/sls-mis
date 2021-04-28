package works.weave.socks.integral.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import works.weave.socks.integral.dao.UsableIntegralDao;
import works.weave.socks.integral.entities.ReduceIntegralDetail;
import works.weave.socks.integral.entities.UsableIntegral;
import works.weave.socks.integral.management.IntegralManagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IntegralScheduled {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    @Autowired
    private UsableIntegralDao usableIntegralDao;
    @Autowired
    private IntegralManagement integralManagement;


    //0 0 0 1/1 * ?
    //0 0/1 * * * ?
    @Scheduled(cron="0 0 0 1/1 * ? ")
    public void expireIntegralTask() {
        System.out.println("do expireIntegralTask");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(new Date());
        List<UsableIntegral> expire = usableIntegralDao.findExpire(format);
        List<Long> idList = expire.stream().map(o -> o.getId()).collect(Collectors.toList());
        List<ReduceIntegralDetail> reduceIntegralDetails = expire.stream().map(O ->
                new ReduceIntegralDetail(O)).collect(Collectors.toList());
        if (expire.size() > 0) {
            integralManagement.batchIntegral(expire, reduceIntegralDetails);
        }
    }


}
