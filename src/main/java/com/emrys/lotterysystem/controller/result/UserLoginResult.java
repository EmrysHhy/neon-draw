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

}
