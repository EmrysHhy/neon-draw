package com.emrys.lotterysystem.dao.mapper;

import com.emrys.lotterysystem.common.utils.AESUtil;
import com.emrys.lotterysystem.dao.dataobject.Encrypt;
import com.emrys.lotterysystem.dao.dataobject.UserDO;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

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

    @Test
    void selectByPhoneNumber() {
        // 看每次加密结果是否不同
        System.out.println("第1次: " + AESUtil.encrypt("12345678901"));
        System.out.println("第2次: " + AESUtil.encrypt("12345678901"));
       UserDO userDO = userMapper.selectByPhoneNumber(new Encrypt("15360167286"));
       System.out.println(userDO.getPhoneNumber().getValue());
    }

    @Test
    void encryptTest() {
        String plain = "15360167286";
        String encrypted = AESUtil.encrypt(plain);
        System.out.println("明文: " + plain);
        System.out.println("密文: " + encrypted);
        System.out.println("解密验证: " + AESUtil.decrypt(encrypted));
    }

    @Test
    void selectByMail() {
        userMapper.selectByMail("m15360167286@163.com");
    }
    @Test
    void selectUser(){
        List<UserDO>  userDOS = userMapper.selectUserListByIdentity(null);
    }

}