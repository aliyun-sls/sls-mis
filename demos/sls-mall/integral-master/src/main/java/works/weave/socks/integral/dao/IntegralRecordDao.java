package works.weave.socks.integral.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import works.weave.socks.integral.entities.IntegralRecord;
import works.weave.socks.integral.entities.UsableIntegral;

import java.util.List;

@Mapper
@Repository
public interface IntegralRecordDao extends BaseDao<IntegralRecord> {

    Float intergralSum(String userId);

    List<IntegralRecord> usableIntergral(String userId);
}
