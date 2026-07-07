package com.emrys.neondraw.controller;

import com.emrys.neondraw.common.pojo.CommonResult;
import com.emrys.neondraw.controller.param.AdminPasswordLoginParam;
import com.emrys.neondraw.controller.param.AdminRegisterParam;
import com.emrys.neondraw.controller.param.CreateGroupParam;
import com.emrys.neondraw.controller.param.MemberRegisterParam;
import com.emrys.neondraw.controller.param.ReNewGroupMembersParam;
import com.emrys.neondraw.controller.result.GroupResult;
import com.emrys.neondraw.controller.result.UserLoginResult;
import com.emrys.neondraw.controller.result.UserRegisterResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GroupControllerTest {

    private static final String TEST_PASSWORD = "abc123";

    @Autowired
    GroupController groupController;
    @Autowired
    AdminController adminController;
    @Autowired
    MemberController memberController;

    private String loginAsAdmin() {
        AdminRegisterParam registerParam = new AdminRegisterParam();
        registerParam.setName("分组测试管理员");
        registerParam.setMail("group" + System.nanoTime() + "@test.com");
        registerParam.setPhoneNumber("137" + String.format("%08d", Math.abs(System.nanoTime() % 100000000L)));
        registerParam.setPassword(TEST_PASSWORD);
        adminController.register(registerParam);

        AdminPasswordLoginParam loginParam = new AdminPasswordLoginParam();
        loginParam.setLoginName(registerParam.getMail());
        loginParam.setPassword(TEST_PASSWORD);
        CommonResult<?> loginResult = adminController.loginByPassword(loginParam);
        return ((UserLoginResult) loginResult.getData()).getToken();
    }

    private Long registerNormalUser(String token) {
        MemberRegisterParam param = new MemberRegisterParam();
        param.setName("分组成员");
        param.setMail("member" + System.nanoTime() + "@test.com");
        param.setPhoneNumber("136" + String.format("%08d", Math.abs(System.nanoTime() % 100000000L)));
        CommonResult<UserRegisterResult> result = memberController.register(param, token);
        return result.getData().getUserId();
    }

    @Test
    void createGroup() {
        String token = loginAsAdmin();
        Long userId = registerNormalUser(token);

        CreateGroupParam param = new CreateGroupParam();
        param.setName("接口测试组_" + System.nanoTime());
        param.setDescription("create 接口测试");
        param.setUserIds(List.of(userId));

        CommonResult<Long> result = groupController.createGroup(param, token);
        System.out.println(result);

        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
    }


    @Test
    void renewGroupMembers() {
        String token = loginAsAdmin();
        Long user1 = registerNormalUser(token);
        Long user2 = registerNormalUser(token);

        CreateGroupParam createParam = new CreateGroupParam();
        createParam.setName("同步组_" + System.nanoTime());
        createParam.setUserIds(List.of(user1));
        Long groupId = groupController.createGroup(createParam, token).getData();

        ReNewGroupMembersParam renewParam = new ReNewGroupMembersParam();
        renewParam.setGroupId(groupId);
        renewParam.setUserIds(List.of(user1, user2));

        CommonResult<Long> result = groupController.renewGroupMembers(renewParam, token);
        System.out.println(result);

        assertEquals(200, result.getCode());
        assertEquals(groupId, result.getData());
    }

    @Test
    void findGroupList() {
        String token = loginAsAdmin();

        CreateGroupParam param = new CreateGroupParam();
        param.setName("列表组_" + System.nanoTime());
        groupController.createGroup(param, token);

        CommonResult<List<GroupResult>> result = groupController.findGroupList(token);
        System.out.println(result);

        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
    }

    @Test
    void deleteGroup() {
        String token = loginAsAdmin();

        CreateGroupParam param = new CreateGroupParam();
        param.setName("待删除组_" + System.nanoTime());
        Long groupId = groupController.createGroup(param, token).getData();

        CommonResult<Boolean> result = groupController.deleteGroup(groupId, token);
        System.out.println(result);

        assertEquals(200, result.getCode());
        assertTrue(result.getData());
    }
}
