package com.emrys.lotterysystem.service.impl;

import com.emrys.lotterysystem.controller.param.UserRegisterParam;
import com.emrys.lotterysystem.service.UserService;
import com.emrys.lotterysystem.service.dto.UserRegisterDTO;
import org.springframework.stereotype.Service;

/**
 *
 * @author Emrys
 * content:具体实现
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserRegisterDTO register(UserRegisterParam param) {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
       userRegisterDTO.setUserId(198L);
        return userRegisterDTO;
    }
}
