package com.emrys.lotterysystem.dao.mapper;

import com.emrys.lotterysystem.dao.dataobject.ActivityDO;
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
public interface ActivityMapper {

    int insert (ActivityDO activityDo);
    @Select("select count(*) from activity")
    int countActivity();
    @Select("select * from activity order by id desc limit #{offset}, #{pageSize}")
    List<ActivityDO> selectActivityList(int offset, Integer pageSize);
    @Select("select * from activity where id = #{activityId}")
    ActivityDO selectById(Long activityId);

    int updateStatusById(@Param("activityId") Long activityId, @Param("status") String status);

}
