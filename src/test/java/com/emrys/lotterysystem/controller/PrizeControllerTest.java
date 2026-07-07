package com.emrys.lotterysystem.controller;

import com.emrys.lotterysystem.common.pojo.CommonResult;
import com.emrys.lotterysystem.controller.param.AdminPasswordLoginParam;
import com.emrys.lotterysystem.controller.param.AdminRegisterParam;
import com.emrys.lotterysystem.controller.param.CreatePrizeParam;
import com.emrys.lotterysystem.controller.param.PageParam;
import com.emrys.lotterysystem.controller.result.FindPrizeListResult;
import com.emrys.lotterysystem.controller.result.UserLoginResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrizeControllerTest {

    private static final String TEST_PASSWORD = "abc123";

    @Autowired
    PrizeController prizeController;
    @Autowired
    AdminController adminController;

    private String loginAsAdmin() {
        AdminRegisterParam registerParam = new AdminRegisterParam();
        registerParam.setName("奖品测试管理员");
        registerParam.setMail("prizeadmin" + System.nanoTime() + "@test.com");
        registerParam.setPhoneNumber("138" + String.format("%08d", Math.abs(System.nanoTime() % 100000000L)));
        registerParam.setPassword(TEST_PASSWORD);
        adminController.register(registerParam);

        AdminPasswordLoginParam loginParam = new AdminPasswordLoginParam();
        loginParam.setLoginName(registerParam.getMail());
        loginParam.setPassword(TEST_PASSWORD);
        CommonResult<?> loginResult = adminController.loginByPassword(loginParam);
        return ((UserLoginResult) loginResult.getData()).getToken();
    }

    @Test
    void createAndFindPrizeListByAdminId() {
        String token = loginAsAdmin();

        CreatePrizeParam param = new CreatePrizeParam();
        param.setPrizeName("测试奖品_" + System.nanoTime());
        param.setDescription("admin 奖品");
        param.setPrice(new BigDecimal("99.99"));

        CommonResult<Long> createResult = prizeController.createPrize(
                token, param, null);
        assertEquals(200, createResult.getCode());
        assertNotNull(createResult.getData());

        PageParam pageParam = new PageParam();
        pageParam.setCurrentPage(1);
        pageParam.setPageSize(10);
        CommonResult<FindPrizeListResult> listResult = prizeController.findPrizeList(token, pageParam);
        assertEquals(200, listResult.getCode());
        assertNotNull(listResult.getData());
        assertFalse(listResult.getData().getRecords().isEmpty());
        assertTrue(listResult.getData().getRecords().stream()
                .anyMatch(p -> param.getPrizeName().equals(p.getPrizeName())));
    }

    @Test
    void findAllPrizeListByAdminId() {
        String token = loginAsAdmin();

        CreatePrizeParam param = new CreatePrizeParam();
        param.setPrizeName("全量奖品_" + System.nanoTime());
        param.setPrice(new BigDecimal("1.00"));
        prizeController.createPrize(token, param, null);

        CommonResult<FindPrizeListResult> allResult = prizeController.findAllPrizeList(token);
        assertEquals(200, allResult.getCode());
        assertNotNull(allResult.getData().getRecords());
        assertTrue(allResult.getData().getRecords().stream()
                .anyMatch(p -> param.getPrizeName().equals(p.getPrizeName())));
    }
}
