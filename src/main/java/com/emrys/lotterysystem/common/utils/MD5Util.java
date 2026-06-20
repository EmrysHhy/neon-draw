package com.emrys.lotterysystem.common.utils;

import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 *
 * @author Emrys
 * content:加密密码 不对称加密
 */
public class MD5Util {
    /**
     * 设置并且返回最终字符串
     * @return
     */
    public static String setAndGetSecret(String plainText){
        String salt = UUID.randomUUID().toString().replace("-","");//生成随机盐值
        String secret1 = plainText + salt;//将明文和盐值拼接
        String result = DigestUtils.md5DigestAsHex(secret1.getBytes());//加密
        return result + salt;
    }
    /**
     *      * 校验密码是否正确
     *      * @param plainText 用户传入的密码明文
     *      * @param result 数据库中的最终字符串密码
     *      * @return boolean
     *      */
    public static boolean verify(String plainText, String result) { //1.用户传入的密码明文, 2.数据库中的最终字符串
        String salt = result.substring(32);//从密文中提取盐值   0~31是密文,32~63是盐值
        String splice = plainText + salt;//将用户这次输入的明文和盐值拼接
        String secret = DigestUtils.md5DigestAsHex(splice.getBytes());//用户这次输入再转成密文
        return (secret + salt).equals(result);//这次最终字符串与数据库值比较
    }

}
