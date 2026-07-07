package com.emrys.lotterysystem.service.impl;

import com.emrys.lotterysystem.common.errorcode.ServiceErrorCodeConstants;
import com.emrys.lotterysystem.common.exception.ServiceException;
import com.emrys.lotterysystem.common.utils.JWTUtil;
import com.emrys.lotterysystem.common.utils.MD5Util;
import com.emrys.lotterysystem.common.utils.RegexUtil;
import com.emrys.lotterysystem.controller.param.AdminPasswordLoginParam;
import com.emrys.lotterysystem.controller.param.AdminRegisterParam;
import com.emrys.lotterysystem.controller.param.AdminVerificationLoginParam;
import com.emrys.lotterysystem.dao.dataobject.AdminDO;
import com.emrys.lotterysystem.dao.dataobject.Encrypt;
import com.emrys.lotterysystem.dao.mapper.AdminMapper;
import com.emrys.lotterysystem.service.AdminService;
import com.emrys.lotterysystem.service.VarificationCodeService;
import com.emrys.lotterysystem.service.dto.UserLoginDTO;
import com.emrys.lotterysystem.service.dto.UserRegisterDTO;
import com.emrys.lotterysystem.service.enums.UserIdentityEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private VarificationCodeService varificationCodeService;

    @Override
    public UserRegisterDTO register(AdminRegisterParam param) {
        checkRegisterInfo(param);
        if (adminMapper.countByMail(param.getMail()) > 0) {
            throw new ServiceException(ServiceErrorCodeConstants.MAIL_HAS_BEEN_USED);
        }
        if (adminMapper.countByPhoneNumber(new Encrypt(param.getPhoneNumber())) > 0) {
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_HAS_BEEN_USED);
        }
        AdminDO adminDO = new AdminDO();
        adminDO.setUserName(param.getName());
        adminDO.setEmail(param.getMail());
        adminDO.setPhoneNumber(new Encrypt(param.getPhoneNumber()));
        adminDO.setPassword(MD5Util.setAndGetSecret(param.getPassword()));
        int ret = adminMapper.insert(adminDO);
        if (ret <= 0) {
            log.warn("管理员注册失败，数据库插入失败，param={}", param);
            return null;
        }
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUserId(adminDO.getId());
        return dto;
    }

    @Override
    public UserLoginDTO login(AdminPasswordLoginParam param) {
        if (param == null) {
            throw new ServiceException(ServiceErrorCodeConstants.LOGIN_INFO_IS_EMPTY);
        }
        AdminDO adminDO = findAdminByLoginName(param.getLoginName());
        if (adminDO == null) {
            log.error("login failed, admin not found, loginName={}", param.getLoginName());
            throw new ServiceException(ServiceErrorCodeConstants.USER_NOT_FOUND);
        }
        if (!MD5Util.verify(param.getPassword(), adminDO.getPassword())) {
            log.error("login failed, password is incorrect, loginName={}", param.getLoginName());
            throw new ServiceException(ServiceErrorCodeConstants.PASSWORD_ERROR);
        }
        return buildLoginDTO(adminDO);
    }

    @Override
    public UserLoginDTO login(AdminVerificationLoginParam param) {
        if (!RegexUtil.checkMail(param.getEmail())) {
            throw new ServiceException(ServiceErrorCodeConstants.MAIL_ERROR);
        }
        AdminDO adminDO = adminMapper.selectByMail(param.getEmail());
        if (adminDO == null) {
            log.error("login failed, admin not found, email={}", param.getEmail());
            throw new ServiceException(ServiceErrorCodeConstants.USER_NOT_FOUND);
        }
        String code = varificationCodeService.getVarificationCode(param.getEmail());
        if (!param.getVerificationCode().equals(code)) {
            log.error("login failed, varification code is incorrect, email={}", param.getEmail());
            throw new ServiceException(ServiceErrorCodeConstants.VARIFICATION_CODE_ERROR);
        }
        return buildLoginDTO(adminDO);
    }

    private AdminDO findAdminByLoginName(String loginName) {
        if (RegexUtil.checkMobile(loginName)) {
            return adminMapper.selectByPhoneNumber(new Encrypt(loginName));
        }
        if (RegexUtil.checkMail(loginName)) {
            return adminMapper.selectByMail(loginName);
        }
        log.error("login failed, loginName is not phone number or email, loginName={}", loginName);
        throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_OR_EMAIL_ERROR);
    }

    private UserLoginDTO buildLoginDTO(AdminDO adminDO) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", adminDO.getId());
        claims.put("identity", UserIdentityEnum.ADMIN.name());
        String token = JWTUtil.genJwtToken(claims);
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setToken(token);
        userLoginDTO.setMandatoryIdentity(UserIdentityEnum.ADMIN);
        return userLoginDTO;
    }

    private void checkRegisterInfo(AdminRegisterParam param) {
        if (param == null) {
            throw new ServiceException(ServiceErrorCodeConstants.REGISTER_INFO_IS_EMPTY);
        }
        if (!RegexUtil.checkMail(param.getMail())) {
            throw new ServiceException(ServiceErrorCodeConstants.MAIL_ERROR);
        }
        if (!RegexUtil.checkMobile(param.getPhoneNumber())) {
            throw new ServiceException(ServiceErrorCodeConstants.PHONE_NUMBER_ERROR);
        }
        if (param.getPassword() == null) {
            throw new ServiceException(ServiceErrorCodeConstants.PASSWORD_EMPTY);
        }
        if (!RegexUtil.checkPassword(param.getPassword())) {
            throw new ServiceException(ServiceErrorCodeConstants.PASSWORD_ERROR);
        }
    }
}
