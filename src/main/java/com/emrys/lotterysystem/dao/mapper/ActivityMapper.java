package com.emrys.lotterysystem.dao.mapper;

import com.emrys.lotterysystem.dao.dataobject.ActivityDo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author Emrys
 * content:
 */
@Mapper
public interface ActivityMapper {

    int insert (ActivityDo activityDo);
}
