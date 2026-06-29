package com.emrys.lotterysystem.dao.mapper;

import com.emrys.lotterysystem.dao.dataobject.PrizeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 *
 * @author Emrys
 * content:
 */
@Mapper
public interface PrizeMapper {
    @Options(useGeneratedKeys=true,keyProperty="id",keyColumn="id")  //可以将自增id加入到prizeDO中
    int insert(PrizeDO prizeDO);
}
