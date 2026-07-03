package com.emrys.lotterysystem.dao.mapper;

import com.emrys.lotterysystem.dao.dataobject.PrizeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
@Mapper
public interface PrizeMapper {
    int insert(PrizeDO prizeDO);

    @Select("select count(*) from prize")
    int countPrize();
    @Select("select * from prize order by id desc limit #{offset}, #{pageSize}")
    List<PrizeDO> selectPrizeList(@Param("offset") int offset, @Param("pageSize") Integer pageSize);

    @Select("select * from prize where id = #{prizeId}")
    PrizeDO selectById(Long prizeId);

    Long countByIds(@Param("ids") List<Long> prizeIds);
    List<PrizeDO> getPrizeListByIds(@Param("ids") List<Long> prizeIds);

    @Select("select * from prize")
    @Options(useGeneratedKeys=true,keyProperty="id",keyColumn="id")  //可以将自增id加入到userDO中
    List<PrizeDO> selectAllPrzieList();

    @Select("select p.* from prize p inner join activity_prize ap on p.id = ap.prize_id where ap.activity_id = #{activityId}")
    List<PrizeDO> selectPrizeListByActivityId(Long activityId);
}
