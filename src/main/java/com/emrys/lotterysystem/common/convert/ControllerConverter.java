package com.emrys.lotterysystem.common.convert;

import com.emrys.lotterysystem.common.errorcode.ControllerErrorCodeConstants;
import com.emrys.lotterysystem.common.exception.ControllerException;
import com.emrys.lotterysystem.controller.result.BaseUserInfoResult;
import com.emrys.lotterysystem.controller.result.UserLoginResult;
import com.emrys.lotterysystem.controller.result.UserRegisterResult;
import com.emrys.lotterysystem.service.dto.UserDTO;
import com.emrys.lotterysystem.service.dto.UserLoginDTO;
import com.emrys.lotterysystem.service.dto.UserRegisterDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
@Slf4j
public class ControllerConverter {
    public static List<BaseUserInfoResult> convertToBaseUserInfoResultList(List<UserDTO> userDTOS) {
        if(userDTOS == null){
            return List.of();
        }
        return userDTOS.stream().map(userDTO -> {
            BaseUserInfoResult baseUserInfoResult = new BaseUserInfoResult();
            baseUserInfoResult.setUserId(userDTO.getUserId());
            baseUserInfoResult.setUserName(userDTO.getUserName());
            baseUserInfoResult.setIdentity(userDTO.getIdentity() != null ? userDTO.getIdentity().name() : "NORMAL");
            return baseUserInfoResult;
        }).toList();
    }

    public static UserLoginResult convertToUserLoginResult(UserLoginDTO userLoginDTO) {
        UserLoginResult userLoginResult = new UserLoginResult();
        if(userLoginDTO == null){
            log.error("user login failed, userLoginDTO is null");
            throw new ControllerException(ControllerErrorCodeConstants.LOGIN_ERROR);
        }
        userLoginResult.setToken(userLoginDTO.getToken());
        userLoginResult.setIdentity(userLoginDTO.getMandatoryIdentity().name());//得到的是一个枚举对象,然后取出name
        return userLoginResult;
    }
    public static UserRegisterResult convertToUserRegisterResult(UserRegisterDTO userRegisterDTO) {
        UserRegisterResult userRegisterResult = new UserRegisterResult();
        if(userRegisterDTO == null){
            log.error("user register failed, userRegisterDTO is null");
            throw new ControllerException(ControllerErrorCodeConstants.REGISTER_ERROR);
        }
        userRegisterResult.setUserId(userRegisterDTO.getUserId());
        return userRegisterResult;
    }
}
