package works.weave.socks.integral.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import works.weave.socks.integral.entities.ReduceIntegralDetail;

import java.util.List;

@Mapper
@Repository
public interface ReduceIntegralDetailDao extends BaseDao<ReduceIntegralDetail> {

    int batchAdd(List<ReduceIntegralDetail> reduceIntegralDetails);

}
