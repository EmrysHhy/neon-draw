package com.emrys.lotterysystem.service;

import com.emrys.lotterysystem.controller.param.UserPasswordLoginParam;
import com.emrys.lotterysystem.controller.param.UserRegisterParam;
import com.emrys.lotterysystem.controller.param.UserVarificationLoginParam;
import com.emrys.lotterysystem.service.dto.UserLoginDTO;
import com.emrys.lotterysystem.service.dto.UserRegisterDTO;
import org.springframework.stereotype.Service;

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
}
