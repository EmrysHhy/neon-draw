package com.emrys.neondraw.service.impl;

import com.emrys.neondraw.common.errorcode.ServiceErrorCodeConstants;
import com.emrys.neondraw.common.exception.ServiceException;
import com.emrys.neondraw.common.utils.JWTUtil;
import com.emrys.neondraw.common.utils.RegexUtil;
import com.emrys.neondraw.controller.param.MemberRegisterParam;
import com.emrys.neondraw.dao.dataobject.Encrypt;
import com.emrys.neondraw.dao.dataobject.NormalUserDO;
import com.emrys.neondraw.dao.mapper.NormalUserMapper;
import com.emrys.neondraw.service.GroupService;
import com.emrys.neondraw.service.MemberService;
import com.emrys.neondraw.service.convert.ServiceConverter;
import com.emrys.neondraw.service.dto.UserDTO;
import com.emrys.neondraw.service.dto.UserRegisterDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private NormalUserMapper normalUserMapper;
    @Autowired
    private GroupService groupService;

    @Override
    public UserRegisterDTO register(MemberRegisterParam param, String adminToken) {
        checkRegisterInfo(param);
        Long adminId = JWTUtil.getAdminId(adminToken);
        NormalUserDO normalUserDO = new NormalUserDO();
        normalUserDO.setUserName(param.getName());
        normalUserDO.setEmail(param.getMail());
        normalUserDO.setPhoneNumber(new Encrypt(param.getPhoneNumber()));
        normalUserDO.setAdminId(adminId);
        int ret = normalUserMapper.insert(normalUserDO);
        if (ret <= 0) {
            log.warn("成员注册失败，数据库插入失败，param={}", param);
            return null;
        }
        if (param.getGroupId() != null) {
            groupService.addUserToGroupMember(adminId, param.getGroupId(), normalUserDO.getId());
        }
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUserId(normalUserDO.getId());
        return dto;
    }

    @Override
    public List<UserDTO> findMemberList(String adminToken, String filter, Long groupId) {
        Long adminId = JWTUtil.getAdminId(adminToken);
        List<NormalUserDO> users;
        if ("none".equalsIgnoreCase(filter)) {
            users = normalUserMapper.selectUngroupedListByAdminId(adminId);
        } else if (groupId != null) {
            users = normalUserMapper.selectListByAdminIdAndGroupId(adminId, groupId);
        } else {
            users = normalUserMapper.selectListByAdminId(adminId);
        }
        return ServiceConverter.convertToUserDTOList(users);
    }

    private void checkRegisterInfo(MemberRegisterParam param) {
        if (param == null) {
            throw new ServiceException(ServiceErrorCodeConstants.REGISTER_INFO_IS_EMPTY);
        }
        if (!RegexUtil.checkMail(param.getMail())) {
            throw new ServiceException(ServiceErrorCodeConstants.MAIL_ERROR);
        }
        if (!RegexUtil.checkMobile(param.getPhoneNumber())) {
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_ERROR);
        }
    }
}
