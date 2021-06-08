package works.weave.socks.antiCheating.controller;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import works.weave.socks.antiCheating.config.AntiCheatingConfigurationProperties;
import works.weave.socks.antiCheating.dao.AntiCheatingRecordDao;
import works.weave.socks.antiCheating.entities.AntiCheatingRecord;
import works.weave.socks.antiCheating.entities.IntegralRecord;
import works.weave.socks.antiCheating.service.AsyncGetService;

import java.net.URI;
import java.util.List;
import java.util.concurrent.Future;


@RepositoryRestController
@RequestMapping("/api/v1")
public class AntiCheatingController {

    Logger LOG = LoggerFactory.getLogger(AntiCheatingController.class);

    @Autowired
    private AntiCheatingRecordDao antiCheatingRecordDao;

    @Autowired
    private AsyncGetService asyncGetService;

    @Autowired
    private AntiCheatingConfigurationProperties config;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/checkIntegral", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Boolean checkIntegral(@RequestBody IntegralRecord integralRecord) {
        LOG.info("anti-cheating[] AntiCheatingController[] checkIntegral IntegralRecord:{} ", integralRecord);
        try {
            AntiCheatingRecord antiCheatingRecord = new AntiCheatingRecord(integralRecord);
            antiCheatingRecordDao.save(antiCheatingRecord);

            Future<String> stringFuture = asyncGetService.postResource(config.getIntegralUri(), integralRecord,
                    new ParameterizedTypeReference<String>() {
                    });
            String result = stringFuture.get();
            LOG.info("anti-cheating[] AntiCheatingController[] checkIntegral result:{} ", result);
        } catch (Exception e) {
            LOG.error("anti-cheating[] AntiCheatingController[] checkIntegral fail:{}",
                    Throwables.getStackTraceAsString(e));
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    @RequestMapping(value = "/listIntegral", method = RequestMethod.GET)
    @ResponseBody
    public List<AntiCheatingRecord> antiCheatingRecordList() {
        return antiCheatingRecordDao.findObjects();
    }
}
