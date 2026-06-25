package com.emrys.lotterysystem.common.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Emrys
 * content:
 */
@Slf4j
public class JWTUtil {

    //    //更推荐使用系统帮我们生成SecretKey,一次使用,终身受益
//    static SecretKey secretKey1 = Keys.secretKeyFor(SignatureAlgorithm.HS256);//参数指定加密算法
//    //将这个秘钥转换回字符串,建议打印出这个字符串保存为常量
//    static String keyString = Encoders.BASE64.encode(secretKey1.getEncoded());
    private static final String KEY_STRING = "KtEcTKlOnRqKp5SKO/fnrPzMQ34YuAffUkI5WD58afs=";
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY_STRING));//通过这个字符串构建key
    private static final long JWT_EXPIRATION = 60 * 60 * 1000;//配置过期的时长

    /**
     * 生成token
     *
     * @param claim
     * @return
     */
    public static String genJwtToken(Map<String, Object> claim) {
        Date now = new Date();
        String token = Jwts.builder()//得到一个构造器
                .claims(claim) //需要传入一个map数据
                .issuedAt(now) // 当前签发时间
                .expiration(new Date(now.getTime() + JWT_EXPIRATION)) // 过期时间
                .signWith(secretKey) // 使用密钥签名
                .compact();//将上面的配置结合
        return token;
    }

    /**
     * 获取token
     *
     * @param token
     * @return
     */
    public static Claims parseToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .verifyWith(secretKey) // 使用同一密钥验签
                    .build()  //通过秘钥生成解析器
                    .parseSignedClaims(token) // 验签并解析
                    .getPayload(); //拿到token第二部分里的数据
        } catch (Exception e) {
            log.error("解析token失败, token:{}", token);
            return null;
        }
        return claims;
    }


}
