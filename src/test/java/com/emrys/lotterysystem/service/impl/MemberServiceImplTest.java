package com.emrys.lotterysystem.service.impl;

import com.emrys.lotterysystem.common.pojo.CommonResult;
import com.emrys.lotterysystem.controller.AdminController;
import com.emrys.lotterysystem.controller.MemberController;
import com.emrys.lotterysystem.controller.param.AdminPasswordLoginParam;
import com.emrys.lotterysystem.controller.param.AdminRegisterParam;
import com.emrys.lotterysystem.controller.param.MemberRegisterParam;
import com.emrys.lotterysystem.controller.result.UserLoginResult;
import com.emrys.lotterysystem.service.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceImplTest {

    private static final String TEST_PASSWORD = "abc123";

    @Autowired
    MemberServiceImpl memberService;
    @Autowired
    AdminController adminController;
    @Autowired
    MemberController memberController;

    private String loginAsAdmin() {
        AdminRegisterParam registerParam = new AdminRegisterParam();
        registerParam.setName("服务层人员列表管理员");
        registerParam.setMail("svcuser" + System.nanoTime() + "@test.com");
        registerParam.setPhoneNumber("133" + String.format("%08d", Math.abs(System.nanoTime() % 100000000L)));
        registerParam.setPassword(TEST_PASSWORD);
        adminController.register(registerParam);

        AdminPasswordLoginParam loginParam = new AdminPasswordLoginParam();
        loginParam.setLoginName(registerParam.getMail());
        loginParam.setPassword(TEST_PASSWORD);
        CommonResult<?> loginResult = adminController.loginByPassword(loginParam);
        return ((UserLoginResult) loginResult.getData()).getToken();
    }

    @Test
    void findMemberList() {
        String token = loginAsAdmin();
        List<UserDTO> userInfo = memberService.findMemberList(token, null, null);
        System.out.println(userInfo);
        assertNotNull(userInfo);
    }
}
