package com.emrys.neondraw.dao.mapper;

import com.emrys.neondraw.dao.dataobject.AdminDO;
import com.emrys.neondraw.dao.dataobject.Encrypt;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

    @Select("select count(*) from admin where email = #{email}")
    int countByMail(@Param("email") String email);

    @Select("select count(*) from admin where phone_number = #{phoneNumber}")
    int countByPhoneNumber(@Param("phoneNumber") Encrypt phoneNumber);

    @Insert("insert into admin(user_name, password, email, phone_number) values (#{userName}, #{password}, #{email}, #{phoneNumber})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(AdminDO adminDO);

    @Select("select * from admin where email = #{email}")
    AdminDO selectByMail(@Param("email") String email);

    @Select("select * from admin where phone_number = #{phoneNumber}")
    AdminDO selectByPhoneNumber(@Param("phoneNumber") Encrypt phoneNumber);

    @Select("select * from admin where id = #{id}")
    AdminDO selectById(@Param("id") Long id);
}
