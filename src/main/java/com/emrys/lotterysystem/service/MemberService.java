package com.emrys.lotterysystem.service;

import com.emrys.lotterysystem.controller.param.MemberRegisterParam;
import com.emrys.lotterysystem.service.dto.UserDTO;
import com.emrys.lotterysystem.service.dto.UserRegisterDTO;

import java.util.List;

public interface MemberService {

    UserRegisterDTO register(MemberRegisterParam param, String adminToken);

    List<UserDTO> findMemberList(String adminToken, String filter, Long groupId);
}
