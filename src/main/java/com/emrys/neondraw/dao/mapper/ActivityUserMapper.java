package com.emrys.neondraw.dao.mapper;

import com.emrys.neondraw.dao.dataobject.ActivityUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
@Mapper
public interface ActivityUserMapper {
    int batchInsert(@Param("list") List<ActivityUserDO> activityUserDOList);

    @Select("select * from activity_user where activity_id = #{activityId}")
    List<ActivityUserDO> selectActivityUserDOListByActivityId(Long activityId);

    List<ActivityUserDO> selectByActivityIdAndUserIds(@Param("activityId") Long activityId,
                                                      @Param("userIds") List<Long> userIds);

    int updateStatusByActivityIdAndUserIds(@Param("activityId") Long activityId,
                                           @Param("userIds") List<Long> userIds,
                                           @Param("status") String status);

}
