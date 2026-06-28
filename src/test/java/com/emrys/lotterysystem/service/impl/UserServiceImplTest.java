package com.emrys.lotterysystem.service.impl;

import com.emrys.lotterysystem.service.dto.UserDTO;
import com.emrys.lotterysystem.service.enums.UserIdentityEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Emrys
 * content:
 */
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    UserServiceImpl userService;
    @Test
    void findUserInfo() {
        List<UserDTO> userInfo = userService.findUserInfo(null);
        System.out.println(userInfo);
        for(UserDTO x: userInfo){
            System.out.println(x.toString());
        }
    }
    @Test
    void test(){
        List<UserDTO> userDTOS = userService.findUserInfo(UserIdentityEnum.forName(null));
        System.out.println(userDTOS);
    }
}