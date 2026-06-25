package com.emrys.lotterysystem.controller.result;

import com.emrys.lotterysystem.common.errorcode.ControllerErrorCodeConstants;
import com.emrys.lotterysystem.common.exception.ControllerException;
import com.emrys.lotterysystem.service.dto.UserLoginDTO;
import com.emrys.lotterysystem.service.dto.UserRegisterDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 *
 * @author Emrys
 * content:作为登录的同一返回
 */
@Data
@Slf4j
public class UserLoginResult implements Serializable {
    private String token;
    private String identity;

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
}
