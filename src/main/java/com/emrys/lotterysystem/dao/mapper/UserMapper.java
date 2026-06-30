package com.emrys.lotterysystem.dao.mapper;

import com.emrys.lotterysystem.dao.dataobject.Encrypt;
import com.emrys.lotterysystem.dao.dataobject.UserDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
@Mapper
public interface UserMapper {
    @Select("select count(*) from user where email = #{email}")
    int countByMail(@Param("email") String email);
    @Select("select count(*) from user where phone_number = #{phoneNumber}")
    int countByPhoneNumber(@Param("phoneNumber") Encrypt phoneNumber);
    @Select("select count(*) from user where user_name = #{userName}")
    int countByName(@Param("userName")String name);

    @Insert("insert into user(user_name,password,email,phone_number,identity) values ( #{userName}, #{password}, #{email}, #{phoneNumber}, #{identity})")
    @Options(useGeneratedKeys=true,keyProperty="id",keyColumn="id")  //可以将自增id加入到userDO中
    int insert(UserDO userDO);
    @Select("select * from user where phone_number = #{phoneNumber}")
    UserDO selectByPhoneNumber(@Param("phoneNumber") Encrypt phoneNumber);
    @Select("select * from user where email = #{email}")
    UserDO selectByMail(String email);
    List<UserDO> selectUserListByIdentity(String identity);
    Long countByIds(@Param("ids") List<Long> ids);
}
