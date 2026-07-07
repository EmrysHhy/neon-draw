package com.emrys.neondraw.dao.mapper;

import com.emrys.neondraw.dao.dataobject.PrizeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PrizeMapper {
    int insert(PrizeDO prizeDO);

    int countPrizeByAdminId(@Param("adminId") Long adminId);

    List<PrizeDO> selectPrizeListByAdminId(@Param("adminId") Long adminId,
                                           @Param("offset") int offset,
                                           @Param("pageSize") Integer pageSize);

    List<PrizeDO> selectAllPrizeListByAdminId(@Param("adminId") Long adminId);

    @Select("select * from prize where id = #{prizeId} and admin_id = #{adminId}")
    PrizeDO selectByIdAndAdminId(@Param("prizeId") Long prizeId, @Param("adminId") Long adminId);

    int deleteByIdAndAdminId(@Param("prizeId") Long prizeId, @Param("adminId") Long adminId);

    @Select("select * from prize where id = #{prizeId}")
    PrizeDO selectById(Long prizeId);

    Long countByIds(@Param("ids") List<Long> prizeIds);

    Long countByIdsAndAdminId(@Param("adminId") Long adminId, @Param("ids") List<Long> prizeIds);

    List<PrizeDO> getPrizeListByIds(@Param("ids") List<Long> prizeIds);

    @Select("select p.* from prize p inner join activity_prize ap on p.id = ap.prize_id where ap.activity_id = #{activityId}")
    List<PrizeDO> selectPrizeListByActivityId(Long activityId);
}
