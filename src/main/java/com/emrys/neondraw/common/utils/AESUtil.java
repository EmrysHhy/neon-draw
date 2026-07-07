package com.emrys.neondraw.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES 加密工具（CBC 模式）
 *
 * 加密手机号 对称加密
 */
public class AESUtil {

    /** AES/CBC/PKCS5Padding */
    private static final String AES_CBC = "AES/CBC/PKCS5Padding";

    /** IV 固定为 16 字节（128 位） */
    private static final int IV_LENGTH = 16;

    /** 预设密钥（16 字节） */
    private static final String DEFAULT_KEY = "lottery2026!@#$%";

    /** 固定 IV（16 字节），确保相同明文产生相同密文 */
    private static final byte[] FIXED_IV = "emrys_iv_16bytes".getBytes(StandardCharsets.UTF_8);

    // ==================== 公开方法 ====================

    /**
     * 加密（使用默认密钥和随机 IV）
     *
     * @param plainText 明文
     * @return Base64(iv + 密文) 拼接字符串
     */
    public static String encrypt(String plainText) {
        return encrypt(plainText, DEFAULT_KEY);
    }

    /**
     * 解密（使用默认密钥，自动从密文中提取 IV）
     *
     * @param cipherText encrypt 方法返回的密文
     * @return 明文
     */
    public static String decrypt(String cipherText) {
        return decrypt(cipherText, DEFAULT_KEY);
    }

    /**
     * 加密（指定密钥）
     *
     * @param plainText 明文
     * @param key       16 字节密钥
     * @return Base64(iv + 密文)
     */
    public static String encrypt(String plainText, String key) {
        if (isEmpty(plainText) || isEmpty(key)) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(FIXED_IV));

            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("AES 加密失败", e);
        }
    }

    /**
     * 解密（指定密钥）
     *
     * @param cipherText encrypt 方法返回的密文
     * @param key        16 字节密钥
     * @return 明文
     */
    public static String decrypt(String cipherText, String key) {
        if (isEmpty(cipherText) || isEmpty(key)) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(FIXED_IV));

            byte[] encrypted = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(encrypted);

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES 解密失败", e);
        }
    }

    // ==================== 内部方法 ====================

    private static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
