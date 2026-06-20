package com.emrys.lotterysystem.service;

import com.emrys.lotterysystem.controller.param.UserRegisterParam;
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
}
