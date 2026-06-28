package com.emrys.lotterysystem.controller.result;

import com.emrys.lotterysystem.common.errorcode.ControllerErrorCodeConstants;
import com.emrys.lotterysystem.common.exception.ControllerException;
import com.emrys.lotterysystem.service.dto.UserRegisterDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 *
 * @author Emrys
 * content:注册的同一返回
 */
@Data
@Slf4j
public class UserRegisterResult implements Serializable {
    Long userId;
    /**
     * 类型转换
     * @param userRegisterDTO ->
     * @return
     */

}
