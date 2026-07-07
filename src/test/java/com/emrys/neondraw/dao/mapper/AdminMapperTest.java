package com.emrys.neondraw.dao.mapper;

import com.emrys.neondraw.dao.dataobject.AdminDO;
import com.emrys.neondraw.dao.dataobject.Encrypt;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdminMapperTest {

    @Autowired
    AdminMapper adminMapper;

    @Test
    void countByMail() {
        int count = adminMapper.countByMail("23113123@qq.com");
        System.out.println(count);
    }

    @Test
    void selectByPhoneNumber() {
        AdminDO adminDO = adminMapper.selectByPhoneNumber(new Encrypt("15360167286"));
        if (adminDO != null) {
            assertNotNull(adminDO.getPhoneNumber().getValue());
        }
    }
}
