package com.emrys.lotterysystem.common.utils;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Emrys
 * content:
 */
@SpringBootTest
class JWTUtilTest {
    @Test
    public void getStringKey(){
            //更推荐使用系统帮我们生成SecretKey,一次使用,终身受益
     SecretKey secretKey1 = Keys.secretKeyFor(SignatureAlgorithm.HS256);//参数指定加密算法
    //将这个秘钥转换回字符串,建议打印出这个字符串保存为常量
     String keyString = Encoders.BASE64.encode(secretKey1.getEncoded());
        System.out.println(keyString);
    }
}