package com.emrys.lotterysystem.service;

import com.emrys.lotterysystem.controller.param.UserPasswordLoginParam;
import com.emrys.lotterysystem.controller.param.UserRegisterParam;
import com.emrys.lotterysystem.controller.param.UserVarificationLoginParam;
import com.emrys.lotterysystem.service.dto.UserDTO;
import com.emrys.lotterysystem.service.dto.UserLoginDTO;
import com.emrys.lotterysystem.service.dto.UserRegisterDTO;
import com.emrys.lotterysystem.service.enums.UserIdentityEnum;

import java.util.List;

/**
 * 用户业务服务接口。
 * <p>
 * 负责用户注册、登录（密码/验证码）以及按身份查询用户列表。
 *
 * @author Emrys
 */
public interface UserService {
    /**
     * 注册用户
     *
     * @param param 注册参数
     * @param token 请求头中的 JWT，注册普通用户时用于解析所属管理员 id
     */
    UserRegisterDTO register(UserRegisterParam param, String token);

    /**
     * 密码登录。
     */
    UserLoginDTO login(UserPasswordLoginParam userPasswordLoginParam);

    /**
     * 验证码登录。
     */
    UserLoginDTO login(UserVarificationLoginParam userVarificationLoginParam);

    /**
     * 按用户身份查询用户列表。
     */
    List<UserDTO> findUserInfo(UserIdentityEnum userIdentityEnum);
}
