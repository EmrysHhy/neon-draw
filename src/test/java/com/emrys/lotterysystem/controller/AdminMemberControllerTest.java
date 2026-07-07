package com.emrys.lotterysystem.controller;

import com.emrys.lotterysystem.common.pojo.CommonResult;
import com.emrys.lotterysystem.controller.param.AdminPasswordLoginParam;
import com.emrys.lotterysystem.controller.param.AdminRegisterParam;
import com.emrys.lotterysystem.controller.param.MemberRegisterParam;
import com.emrys.lotterysystem.controller.result.BaseUserInfoResult;
import com.emrys.lotterysystem.controller.result.UserLoginResult;
import com.emrys.lotterysystem.service.enums.UserIdentityEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminMemberControllerTest {

    private static final String TEST_PASSWORD = "abc123";

    @Autowired
    AdminController adminController;
    @Autowired
    MemberController memberController;

    private String loginAsAdmin() {
        AdminRegisterParam registerParam = new AdminRegisterParam();
        registerParam.setName("分表测试管理员");
        registerParam.setMail("splitadmin" + System.nanoTime() + "@test.com");
        registerParam.setPhoneNumber("135" + String.format("%08d", Math.abs(System.nanoTime() % 100000000L)));
        registerParam.setPassword(TEST_PASSWORD);
        adminController.register(registerParam);

        AdminPasswordLoginParam loginParam = new AdminPasswordLoginParam();
        loginParam.setLoginName(registerParam.getMail());
        loginParam.setPassword(TEST_PASSWORD);
        CommonResult<?> loginResult = adminController.loginByPassword(loginParam);
        return ((UserLoginResult) loginResult.getData()).getToken();
    }

    @Test
    void adminLoginWithUniqueEmail() {
        String sharedMail = "adminuniq" + System.nanoTime() + "@test.com";
        String phone = "131" + String.format("%08d", Math.abs(System.nanoTime() % 100000000L));

        AdminRegisterParam adminParam = new AdminRegisterParam();
        adminParam.setName("唯一管理员");
        adminParam.setMail(sharedMail);
        adminParam.setPhoneNumber(phone);
        adminParam.setPassword(TEST_PASSWORD);
        assertEquals(200, adminController.register(adminParam).getCode());

        AdminPasswordLoginParam loginParam = new AdminPasswordLoginParam();
        loginParam.setLoginName(sharedMail);
        loginParam.setPassword(TEST_PASSWORD);
        CommonResult<?> loginResult = adminController.loginByPassword(loginParam);
        assertEquals(200, loginResult.getCode());
        assertEquals(UserIdentityEnum.ADMIN.name(), ((UserLoginResult) loginResult.getData()).getIdentity());
    }

    @Test
    void memberRegisterAllowsDuplicateEmail() {
        String token = loginAsAdmin();
        String sharedMail = "memberdup" + System.nanoTime() + "@test.com";

        MemberRegisterParam param1 = new MemberRegisterParam();
        param1.setName("成员甲");
        param1.setMail(sharedMail);
        param1.setPhoneNumber("132" + String.format("%08d", Math.abs(System.nanoTime() % 100000000L)));
        assertEquals(200, memberController.register(param1, token).getCode());

        MemberRegisterParam param2 = new MemberRegisterParam();
        param2.setName("成员乙");
        param2.setMail(sharedMail);
        param2.setPhoneNumber("133" + String.format("%08d", Math.abs(System.nanoTime() % 100000000L)));
        assertEquals(200, memberController.register(param2, token).getCode());

        CommonResult<List<BaseUserInfoResult>> list = memberController.findMemberList(token, null, null);
        assertEquals(200, list.getCode());
        assertTrue(list.getData().size() >= 2);
    }

    @Test
    void findMemberList() {
        String token = loginAsAdmin();

        MemberRegisterParam normalParam = new MemberRegisterParam();
        normalParam.setName("下属成员");
        normalParam.setMail("member" + System.nanoTime() + "@test.com");
        normalParam.setPhoneNumber("134" + String.format("%08d", Math.abs(System.nanoTime() % 100000000L)));
        memberController.register(normalParam, token);

        CommonResult<List<BaseUserInfoResult>> result = memberController.findMemberList(token, null, null);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertFalse(result.getData().isEmpty());
        assertTrue(result.getData().stream()
                .allMatch(user -> UserIdentityEnum.NORMAL.name().equalsIgnoreCase(user.getIdentity())));
    }
}
