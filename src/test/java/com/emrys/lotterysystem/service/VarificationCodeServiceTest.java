package com.emrys.lotterysystem.service;

import com.emrys.lotterysystem.service.impl.VarificationCodeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Emrys
 * content:
 */
@SpringBootTest
class VarificationCodeServiceTest {

    @Autowired
    VarificationCodeService varificationCodeService;
    @Test
    void sendVarificationCode() {
        String e = "2892551974@qq.com";
        varificationCodeService.sendVarificationCode(e);
    }

    @Test
    void getVarificationCode() {
    }
}