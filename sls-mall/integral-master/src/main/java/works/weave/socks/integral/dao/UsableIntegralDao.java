package works.weave.socks.integral.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import works.weave.socks.integral.entities.UsableIntegral;

import java.util.List;

@Mapper
@Repository
public interface UsableIntegralDao extends BaseDao<UsableIntegral> {

    Float intergralSum(String userId);

    List<UsableIntegral> usableIntergral(String userId);

    List<UsableIntegral> findExpire(String expireTime);

    void updateExpire(List<UsableIntegral> usableIntegrals);
}
