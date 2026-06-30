package com.emrys.lotterysystem.dao.mapper;

import com.emrys.lotterysystem.dao.dataobject.ActivityPrizeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
@Mapper
public interface ActivityPrizeMapper {

    int batchInsert(@Param("list") List<ActivityPrizeDO> activityPrizeDOList);
}
