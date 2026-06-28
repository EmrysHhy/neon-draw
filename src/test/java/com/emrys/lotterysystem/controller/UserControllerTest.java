package com.emrys.lotterysystem.controller;

import com.emrys.lotterysystem.common.pojo.CommonResult;
import com.emrys.lotterysystem.controller.result.BaseUserInfoResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Emrys
 * content:
 */
@SpringBootTest
class UserControllerTest {
    @Autowired
    UserController userController;
    @Test
    void findBaseUserInfo() {
        CommonResult<List<BaseUserInfoResult>> baseUserInfo = userController.findBaseUserInfo(null);
        baseUserInfo.getData().forEach(System.out::println);
    }
}