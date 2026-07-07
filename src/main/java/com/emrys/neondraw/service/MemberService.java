package com.emrys.neondraw.service;

import com.emrys.neondraw.controller.param.MemberRegisterParam;
import com.emrys.neondraw.service.dto.UserDTO;
import com.emrys.neondraw.service.dto.UserRegisterDTO;

import java.util.List;

public interface MemberService {

    UserRegisterDTO register(MemberRegisterParam param, String adminToken);

    List<UserDTO> findMemberList(String adminToken, String filter, Long groupId);
}
