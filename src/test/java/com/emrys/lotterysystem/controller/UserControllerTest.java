package com.emrys.lotterysystem.controller;

import com.emrys.lotterysystem.common.pojo.CommonResult;
import com.emrys.lotterysystem.controller.param.UserPasswordLoginParam;
import com.emrys.lotterysystem.controller.param.UserRegisterParam;
import com.emrys.lotterysystem.controller.result.BaseUserInfoResult;
import com.emrys.lotterysystem.controller.result.UserLoginResult;
import com.emrys.lotterysystem.controller.result.UserRegisterResult;
import com.emrys.lotterysystem.service.enums.UserIdentityEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    private static final String TEST_PASSWORD = "abc123";

    @Autowired
    UserController userController;

    @Test
    void findBaseUserInfo() {
        CommonResult<List<BaseUserInfoResult>> baseUserInfo = userController.findBaseUserInfo(null);
        baseUserInfo.getData().forEach(System.out::println);
    }



}
