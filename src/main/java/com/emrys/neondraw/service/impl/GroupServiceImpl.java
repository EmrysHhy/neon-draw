package com.emrys.neondraw.service.impl;

import com.emrys.neondraw.common.errorcode.ServiceErrorCodeConstants;
import com.emrys.neondraw.common.exception.ServiceException;
import com.emrys.neondraw.common.utils.JWTUtil;
import com.emrys.neondraw.common.utils.RedisUtil;
import com.emrys.neondraw.controller.param.CreateGroupParam;
import com.emrys.neondraw.controller.param.ReNewGroupMembersParam;
import com.emrys.neondraw.dao.dataobject.GroupMemberDO;
import com.emrys.neondraw.dao.dataobject.NormalUserDO;
import com.emrys.neondraw.dao.dataobject.UserGroupDO;
import com.emrys.neondraw.dao.mapper.GroupMapper;
import com.emrys.neondraw.dao.mapper.GroupMemberMapper;
import com.emrys.neondraw.dao.mapper.NormalUserMapper;
import com.emrys.neondraw.service.GroupService;
import com.emrys.neondraw.service.dto.GroupDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.emrys.neondraw.service.constants.RedisConstant.GROUP_DETAIL_;
import static com.emrys.neondraw.service.constants.RedisConstant.GROUP_DETAIL_TIMEOUT;

// group 表存小组信息（id 自增）；group_member 表存成员关系（一行 = 一个用户在某个组内）
// 以下所有操作都限定在当前登录管理员（token 里的 adminId）范围内
@Slf4j
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    @Autowired
    private NormalUserMapper normalUserMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createGroup(CreateGroupParam param, String token) {
        // 从 token 取当前管理员 id
        Long adminId = JWTUtil.getAdminId(token);
        // 同一管理员下组名不能重复
        if (groupMapper.countByAdminIdAndName(adminId, param.getName()) > 0) {
            throw new ServiceException(ServiceErrorCodeConstants.GROUP_NAME_DUPLICATE);
        }
        // 写入 group 表，id 由数据库自增
        UserGroupDO userGroupDO = new UserGroupDO();
        userGroupDO.setAdminId(adminId);
        userGroupDO.setName(param.getName());
        userGroupDO.setDescription(param.getDescription());
        int ret = groupMapper.insert(userGroupDO);
        if (ret <= 0) {
            throw new ServiceException(ServiceErrorCodeConstants.GROUP_CREATE_FAILED);
        }
        // 创建时若带了 userIds，则一并写入 group_member
        if (!CollectionUtils.isEmpty(param.getUserIds())) {
            ReNewGroupMembersParam renewParam = new ReNewGroupMembersParam();
            renewParam.setGroupId(userGroupDO.getId());
            renewParam.setUserIds(param.getUserIds());
            renewGroupMembers(renewParam, token);
        } else {
            refreshGroupDetailCache(userGroupDO.getId(), adminId);
        }
        return userGroupDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long renewGroupMembers(ReNewGroupMembersParam param, String token) {
        Long adminId = JWTUtil.getAdminId(token);
        List<Long> userIds = param.getUserIds();
        // userIds 为 null 时不做变更；空列表表示清空该组全部成员
        if (userIds == null) {
            return param.getGroupId();
        }
        requireGroupOwnedByAdmin(param.getGroupId(), adminId);
        if (userIds.isEmpty()) {
            groupMemberMapper.deleteByGroupIdAndUserIdsNotIn(param.getGroupId(), userIds);
            refreshGroupDetailCache(param.getGroupId(), adminId);
            return param.getGroupId();
        }
        validateNormalUsers(adminId, userIds);
        // 记录同步前已有成员，后面只 insert 差集（写路径直接查 DB，不走缓存）
        Set<Long> existingUserIds = new HashSet<>(groupMemberMapper.selectUserIdsByGroupId(param.getGroupId()));
        // 删掉不在目标列表里的成员
        groupMemberMapper.deleteByGroupIdAndUserIdsNotIn(param.getGroupId(), userIds);
        List<GroupMemberDO> toAdd = new ArrayList<>();
        for (Long userId : userIds) {
            if (!existingUserIds.contains(userId)) {
                GroupMemberDO memberDO = new GroupMemberDO();
                memberDO.setGroupId(param.getGroupId());
                memberDO.setUserId(userId);
                toAdd.add(memberDO);
            }
        }
        if (!toAdd.isEmpty()) {
            groupMemberMapper.batchInsert(toAdd);
        }
        refreshGroupDetailCache(param.getGroupId(), adminId);
        return param.getGroupId();
    }

    @Override
    public List<GroupDTO> findGroupList(String token) {
        Long adminId = JWTUtil.getAdminId(token);
        // 查当前管理员下所有小组
        List<UserGroupDO> groups = groupMapper.selectByAdminId(adminId);
        List<GroupDTO> result = new ArrayList<>();
        for (UserGroupDO group : groups) {
            result.add(getGroupDetail(group.getId(), adminId));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGroup(Long groupId, String token) {
        Long adminId = JWTUtil.getAdminId(token);
        requireGroupOwnedByAdmin(groupId, adminId);
        // 先删成员关系，再删小组
        groupMemberMapper.deleteByGroupId(groupId);
        int ret = groupMapper.deleteByIdAndAdminId(groupId, adminId);
        if (ret <= 0) {
            throw new ServiceException(ServiceErrorCodeConstants.GROUP_DELETE_FAILED);
        }
        evictGroupDetailCache(groupId);
    }

    // 注册普通用户时使用：只追加一名成员，不做全量覆盖
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserToGroupMember(Long adminId, Long groupId, Long userId) {
        // 未选组则跳过
        if (groupId == null) {
            return;
        }
        // 小组必须属于当前管理员
        requireGroupOwnedByAdmin(groupId, adminId);
        // 用户必须存在、身份为 NORMAL、且 admin_id 一致
        validateNormalUsers(adminId, List.of(userId));
        Set<Long> existing = new HashSet<>(groupMemberMapper.selectUserIdsByGroupId(groupId));
        // 已在组内，幂等返回
        if (existing.contains(userId)) {
            return;
        }
        GroupMemberDO memberDO = new GroupMemberDO();
        memberDO.setGroupId(groupId);
        memberDO.setUserId(userId);
        groupMemberMapper.insert(memberDO);
        refreshGroupDetailCache(groupId, adminId);
    }

    private GroupDTO getGroupDetail(Long groupId, Long adminId) {
        GroupDTO cached = getGroupDetailFromCache(groupId);
        if (cached != null) {
            return cached;
        }
        log.info("无法从缓存中获取小组详情,从数据库中加载, groupId: {}", groupId);
        return loadGroupDetailFromDb(groupId, adminId);
    }

    private GroupDTO getGroupDetailFromCache(Long groupId) {
        String key = GROUP_DETAIL_ + groupId;
        String value = redisUtil.get(key);
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.readValue(value, GroupDTO.class);
        } catch (Exception e) {
            log.error("get group detail from redis failed, key is {}, error is {}", key, e.getMessage());
            return null;
        }
    }

    private GroupDTO loadGroupDetailFromDb(Long groupId, Long adminId) {
        UserGroupDO group = requireGroupOwnedByAdmin(groupId, adminId);
        List<Long> memberUserIds = groupMemberMapper.selectUserIdsByGroupId(groupId);
        GroupDTO dto = buildGroupDTO(group, memberUserIds);
        cacheGroupDetail(dto);
        return dto;
    }

    private void refreshGroupDetailCache(Long groupId, Long adminId) {
        loadGroupDetailFromDb(groupId, adminId);
    }

    private void evictGroupDetailCache(Long groupId) {
        redisUtil.delete(GROUP_DETAIL_ + groupId);
    }

    private void cacheGroupDetail(GroupDTO groupDTO) {
        if (groupDTO == null || groupDTO.getGroupId() == null) {
            log.error("cache group detail failed, groupDTO is null or groupId is null");
            return;
        }
        try {
            redisUtil.set(
                    GROUP_DETAIL_ + groupDTO.getGroupId(),
                    objectMapper.writeValueAsString(groupDTO),
                    GROUP_DETAIL_TIMEOUT);
        } catch (Exception e) {
            log.error("cache group detail failed, groupDTO is {}, error is {}", groupDTO, e.getMessage());
        }
    }

    private GroupDTO buildGroupDTO(UserGroupDO group, List<Long> memberUserIds) {
        GroupDTO dto = new GroupDTO();
        dto.setGroupId(group.getId());
        dto.setName(group.getName());
        dto.setDescription(group.getDescription());
        dto.setUserIds(memberUserIds);
        dto.setMemberCount(memberUserIds.size());
        return dto;
    }

    // 校验 groupId 存在且 admin_id 匹配，否则抛 GROUP_NOT_FOUND
    private UserGroupDO requireGroupOwnedByAdmin(Long groupId, Long adminId) {
        UserGroupDO group = groupMapper.selectByIdAndAdminId(groupId, adminId);
        if (group == null) {
            throw new ServiceException(ServiceErrorCodeConstants.GROUP_NOT_FOUND);
        }
        return group;
    }

    // 校验 userIds：非空、用户存在、身份 NORMAL、归属同一管理员
    private void validateNormalUsers(Long adminId, List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            throw new ServiceException(ServiceErrorCodeConstants.GROUP_MEMBER_EMPTY);
        }
        List<NormalUserDO> users = normalUserMapper.selectByIds(userIds);
        // 有 id 查不到说明用户不存在
        if (users.size() != userIds.size()) {
            throw new ServiceException(ServiceErrorCodeConstants.GROUP_MEMBER_NOT_FOUND);
        }
        for (NormalUserDO user : users) {
            if (!Objects.equals(adminId, user.getAdminId())) {
                throw new ServiceException(ServiceErrorCodeConstants.GROUP_MEMBER_NOT_BELONG_ADMIN);
            }
        }
    }
}
