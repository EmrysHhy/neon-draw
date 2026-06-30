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
    @Select("select count(*) from prize where id in (#{prizeIds})")
    Long countByIds(List<Long> prizeIds);
}
