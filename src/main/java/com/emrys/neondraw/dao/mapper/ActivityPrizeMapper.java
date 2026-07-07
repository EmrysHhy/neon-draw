package com.emrys.neondraw.dao.mapper;

import com.emrys.neondraw.dao.dataobject.ActivityPrizeDO;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
@Mapper
public interface ActivityPrizeMapper {

    int batchInsert(@Param("list") List<ActivityPrizeDO> activityPrizeDOList);

    @Select("select count(*) from activity_prize where prize_id = #{prizeId} and status = #{status}")
    int countByPrizeIdAndStatus(@Param("prizeId") Long prizeId, @Param("status") String status);

    @Select("select * from activity_prize where activity_id = #{activityId}")
    List<ActivityPrizeDO> selectActivityPrizeDOListByActivityId(Long activityId);
    @Select("select * from activity_prize where activity_id = #{activityId} and prize_id = #{prizeId}")
    ActivityPrizeDO selectByActivityIdAndPrizeId(@NotNull(message = "活动id不能为空") Long activityId, @NotNull(message = "奖品id不能为空") Long prizeId);
    @Select("select count(*) from activity_prize where activity_id = #{activityId} and prize_id = #{prizeId} and status = #{status}")
    int countByActivityIdAndPrizeIdAndStatus(@Param("activityId") Long activityId,
                                           @Param("prizeId") Long prizeId,
                                           @Param("status") String status);
    @Select("select count(*) from activity_prize where activity_id = #{activityId} and status = #{status}")
    int countByActivityIdAndStatus(@Param("activityId") Long activityId, @Param("status") String status);
    @Update("update activity_prize set status = #{status} where activity_id = #{activityId} and prize_id = #{prizeId}")
    int updateStatusByActivityIdAndPrizeId(@Param("activityId") Long activityId,
                                           @Param("prizeId") Long prizeId,
                                           @Param("status") String status);
}
