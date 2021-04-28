package works.weave.socks.antiCheating.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import works.weave.socks.antiCheating.entities.AntiCheatingRecord;

import java.util.List;

@Mapper
@Repository
public interface AntiCheatingRecordDao extends BaseDao<AntiCheatingRecord> {

}
