package com.emrys.neondraw.dao.mapper;

import com.emrys.neondraw.dao.dataobject.NormalUserDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NormalUserMapper {

    @Insert("insert into normal_user(user_name, email, phone_number, admin_id) values (#{userName}, #{email}, #{phoneNumber}, #{adminId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(NormalUserDO normalUserDO);

    List<NormalUserDO> selectListByAdminId(@Param("adminId") Long adminId);

    List<NormalUserDO> selectUngroupedListByAdminId(@Param("adminId") Long adminId);

    List<NormalUserDO> selectListByAdminIdAndGroupId(@Param("adminId") Long adminId, @Param("groupId") Long groupId);

    Long countByIds(@Param("ids") List<Long> ids);

    List<NormalUserDO> selectByIds(@Param("ids") List<Long> ids);
}
