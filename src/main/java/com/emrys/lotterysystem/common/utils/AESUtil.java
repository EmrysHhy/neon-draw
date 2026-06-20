package com.emrys.lotterysystem.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
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

            // 生成随机 IV
            byte[] iv = generateIv();
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));

            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // IV + 密文 拼在一起后 Base64 编码
            byte[] combined = new byte[IV_LENGTH + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, IV_LENGTH);
            System.arraycopy(encrypted, 0, combined, IV_LENGTH, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);
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

            byte[] combined = Base64.getDecoder().decode(cipherText);

            // 前 16 字节是 IV，后面是密文
            byte[] iv = new byte[IV_LENGTH];
            byte[] encrypted = new byte[combined.length - IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, IV_LENGTH);
            System.arraycopy(combined, IV_LENGTH, encrypted, 0, encrypted.length);

            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
            byte[] decrypted = cipher.doFinal(encrypted);

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES 解密失败", e);
        }
    }

    // ==================== 内部方法 ====================

    private static byte[] generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
