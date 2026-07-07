package com.emrys.neondraw.dao.mapper;

import com.emrys.neondraw.dao.dataobject.UserGroupDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupMapper {

    @Insert("insert into `group`(admin_id, name, description) values (#{adminId}, #{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(UserGroupDO userGroupDO);

    @Select("select * from `group` where admin_id = #{adminId} order by id desc")
    List<UserGroupDO> selectByAdminId(@Param("adminId") Long adminId);
    
    @Select("select * from `group` where id = #{id}")
    UserGroupDO selectById(@Param("id") Long id);

    @Select("select * from `group` where id = #{id} and admin_id = #{adminId}")
    UserGroupDO selectByIdAndAdminId(@Param("id") Long id, @Param("adminId") Long adminId);

    @Select("select count(*) from `group` where admin_id = #{adminId} and name = #{name}")
    int countByAdminIdAndName(@Param("adminId") Long adminId, @Param("name") String name);

    @Delete("delete from `group` where id = #{id} and admin_id = #{adminId}")
    int deleteByIdAndAdminId(@Param("id") Long id, @Param("adminId") Long adminId);
}
