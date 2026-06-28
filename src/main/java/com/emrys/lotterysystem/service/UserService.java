package com.emrys.lotterysystem.service;

import com.emrys.lotterysystem.controller.param.UserPasswordLoginParam;
import com.emrys.lotterysystem.controller.param.UserRegisterParam;
import com.emrys.lotterysystem.controller.param.UserVarificationLoginParam;
import com.emrys.lotterysystem.service.dto.UserDTO;
import com.emrys.lotterysystem.service.dto.UserLoginDTO;
import com.emrys.lotterysystem.service.dto.UserRegisterDTO;
import com.emrys.lotterysystem.service.enums.UserIdentityEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author Emrys
 * content:Service接口
 */
public interface UserService {
    /**
     * 注册用户
     */
    UserRegisterDTO register(UserRegisterParam param);

    /**
     * 密码登录和验证码登录接口
     * @return UserLoginDTO
     */
    UserLoginDTO login(UserPasswordLoginParam userPasswordLoginParam);
    UserLoginDTO login(UserVarificationLoginParam userVarificationLoginParam);

    List<UserDTO> findUserInfo(UserIdentityEnum userIdentityEnum);
}
