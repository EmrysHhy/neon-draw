package com.emrys.lotterysystem.dao.mapper;

import com.emrys.lotterysystem.dao.dataobject.Encrypt;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Emrys
 * content:
 */
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    @Test
    void countByMail() {
        int count = userMapper.countByMail("23113123@qq.com");
        System.out.println(count);
    }

    @Test
    void countByPhoneNumber() {
        int count = userMapper.countByPhoneNumber(new Encrypt("12345678901"));
        System.out.println(count);
    }
}