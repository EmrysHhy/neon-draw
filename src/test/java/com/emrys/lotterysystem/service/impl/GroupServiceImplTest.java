package com.emrys.lotterysystem.service.impl;

import com.emrys.lotterysystem.common.pojo.CommonResult;
import com.emrys.lotterysystem.common.utils.RedisUtil;
import com.emrys.lotterysystem.controller.AdminController;
import com.emrys.lotterysystem.controller.GroupController;
import com.emrys.lotterysystem.controller.MemberController;
import com.emrys.lotterysystem.controller.param.AdminPasswordLoginParam;
import com.emrys.lotterysystem.controller.param.AdminRegisterParam;
import com.emrys.lotterysystem.controller.param.CreateGroupParam;
import com.emrys.lotterysystem.controller.param.MemberRegisterParam;
import com.emrys.lotterysystem.controller.param.ReNewGroupMembersParam;
import com.emrys.lotterysystem.controller.result.UserLoginResult;
import com.emrys.lotterysystem.service.dto.GroupDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.emrys.lotterysystem.service.constants.RedisConstant.GROUP_DETAIL_;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GroupServiceImplTest {

    private static final String TEST_PASSWORD = "abc123";

    @Autowired
    GroupServiceImpl groupService;
    @Autowired
    GroupController groupController;
    @Autowired
    AdminController adminController;
    @Autowired
    MemberController memberController;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ObjectMapper objectMapper;

    private String loginAsAdmin() {
        AdminRegisterParam registerParam = new AdminRegisterParam();
        registerParam.setName("缓存测试管理员");
        registerParam.setMail("grpcache" + System.nanoTime() + "@test.com");
        registerParam.setPhoneNumber("135" + String.format("%08d", Math.abs(System.nanoTime() % 100000000L)));
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
        param.setName("缓存测试成员");
        param.setMail("grpcachemember" + System.nanoTime() + "@test.com");
        param.setPhoneNumber("134" + String.format("%08d", Math.abs(System.nanoTime() % 100000000L)));
        return memberController.register(param, token).getData().getUserId();
    }

    @Test
    void findGroupListCachesGroupDetail() throws Exception {
        String token = loginAsAdmin();
        Long userId = registerNormalUser(token);

        CreateGroupParam createParam = new CreateGroupParam();
        createParam.setName("缓存组_" + System.nanoTime());
        createParam.setDescription("缓存测试");
        createParam.setUserIds(List.of(userId));
        Long groupId = groupController.createGroup(createParam, token).getData();
        assertNotNull(groupId);

        String cacheKey = GROUP_DETAIL_ + groupId;
        assertTrue(redisUtil.hasKey(cacheKey));

        GroupDTO cached = objectMapper.readValue(redisUtil.get(cacheKey), GroupDTO.class);
        assertEquals(groupId, cached.getGroupId());
        assertEquals(1, cached.getMemberCount());
        assertEquals(List.of(userId), cached.getUserIds());

        redisUtil.delete(cacheKey);
        assertFalse(redisUtil.hasKey(cacheKey));

        List<GroupDTO> list = groupService.findGroupList(token);
        assertTrue(redisUtil.hasKey(cacheKey));
        GroupDTO fromList = list.stream()
                .filter(g -> groupId.equals(g.getGroupId()))
                .findFirst()
                .orElseThrow();
        assertEquals(cached.getUserIds(), fromList.getUserIds());
    }

    @Test
    void renewGroupMembersRefreshesCache() throws Exception {
        String token = loginAsAdmin();
        Long user1 = registerNormalUser(token);
        Long user2 = registerNormalUser(token);

        CreateGroupParam createParam = new CreateGroupParam();
        createParam.setName("刷新缓存组_" + System.nanoTime());
        createParam.setUserIds(List.of(user1));
        Long groupId = groupController.createGroup(createParam, token).getData();

        groupService.findGroupList(token);
        String cacheKey = GROUP_DETAIL_ + groupId;

        ReNewGroupMembersParam renewParam = new ReNewGroupMembersParam();
        renewParam.setGroupId(groupId);
        renewParam.setUserIds(List.of(user1, user2));
        groupController.renewGroupMembers(renewParam, token);

        GroupDTO cached = objectMapper.readValue(redisUtil.get(cacheKey), GroupDTO.class);
        assertEquals(2, cached.getMemberCount());
        assertTrue(cached.getUserIds().containsAll(List.of(user1, user2)));
    }

    @Test
    void deleteGroupEvictsCache() {
        String token = loginAsAdmin();

        CreateGroupParam createParam = new CreateGroupParam();
        createParam.setName("删除缓存组_" + System.nanoTime());
        Long groupId = groupController.createGroup(createParam, token).getData();

        groupService.findGroupList(token);
        String cacheKey = GROUP_DETAIL_ + groupId;
        assertTrue(redisUtil.hasKey(cacheKey));

        groupController.deleteGroup(groupId, token);
        assertFalse(redisUtil.hasKey(cacheKey));
    }
}
