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
    int insert(PrizeDO prizeDO);
}
