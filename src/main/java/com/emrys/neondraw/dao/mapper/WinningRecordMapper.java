package com.emrys.neondraw.dao.mapper;

import com.emrys.neondraw.dao.dataobject.WinningRecordDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 中奖记录表 winning_record
 *
 * @author Emrys
 */
@Mapper
public interface WinningRecordMapper {

    int batchInsert(@Param("list") List<WinningRecordDO> winnerRecordDOList);

    List<WinningRecordDO> selectByActivityId(@Param("activityId") Long activityId);

    List<WinningRecordDO> selectByActivityIdAndPrizeId(@Param("activityId") Long activityId,
                                                       @Param("prizeId") Long prizeId);
}
