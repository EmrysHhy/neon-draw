package com.emrys.lotterysystem.dao.mapper;

import com.emrys.lotterysystem.dao.dataobject.GroupMemberDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupMemberMapper {

    @Insert("insert into group_member(group_id, user_id) values (#{groupId}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(GroupMemberDO groupMemberDO);

    int batchInsert(@Param("list") List<GroupMemberDO> list);

    int deleteByGroupIdAndUserIdsNotIn(@Param("groupId") Long groupId, @Param("userIds") List<Long> userIds);

    @Select("select user_id from group_member where group_id = #{groupId}")
    List<Long> selectUserIdsByGroupId(@Param("groupId") Long groupId);

    @Select("select count(*) from group_member where group_id = #{groupId}")
    int countByGroupId(@Param("groupId") Long groupId);

    @Delete("delete from group_member where group_id = #{groupId}")
    int deleteByGroupId(@Param("groupId") Long groupId);
}
