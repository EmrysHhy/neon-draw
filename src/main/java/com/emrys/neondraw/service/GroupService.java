package com.emrys.neondraw.service;

import com.emrys.neondraw.controller.param.CreateGroupParam;
import com.emrys.neondraw.controller.param.ReNewGroupMembersParam;
import com.emrys.neondraw.service.dto.GroupDTO;

import java.util.List;

public interface GroupService {

    Long createGroup(CreateGroupParam param, String token);

    Long renewGroupMembers(ReNewGroupMembersParam param, String token);

    List<GroupDTO> findGroupList(String token);

    void deleteGroup(Long groupId, String token);

    void addUserToGroupMember(Long adminId, Long groupId, Long userId);
}
