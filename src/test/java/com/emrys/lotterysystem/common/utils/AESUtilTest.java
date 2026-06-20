package com.emrys.lotterysystem.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Emrys
 */
@Slf4j
class AESUtilTest {

    @Test
    void encryptAndDecrypt() {
        String text = "Hello, 抽奖系统!";
        String encrypted = AESUtil.encrypt(text);
        log.info("密文: {}", encrypted);

        String decrypted = AESUtil.decrypt(encrypted);
        log.info("明文: {}", decrypted);

        assertEquals(text, decrypted);
    }

    @Test
    void encryptAndDecryptWithCustomKey() {
        String text = "自定义密钥测试";
        String key = "mySecretKey12345";  // 16 字节

        String encrypted = AESUtil.encrypt(text, key);
        log.info("密文: {}", encrypted);

        String decrypted = AESUtil.decrypt(encrypted, key);
        log.info("明文: {}", decrypted);

        assertEquals(text, decrypted);
    }

    @Test
    void sameTextProducesDifferentCipher() {
        String text = "abc";
        String enc1 = AESUtil.encrypt(text);
        String enc2 = AESUtil.encrypt(text);

        assertNotEquals(enc1, enc2, "CBC + 随机 IV，相同明文应产生不同密文");
        assertEquals(text, AESUtil.decrypt(enc1));
        assertEquals(text, AESUtil.decrypt(enc2));
    }

    @Test
    void nullOrEmpty() {
        assertNull(AESUtil.encrypt(null));
        assertNull(AESUtil.encrypt(""));
        assertNull(AESUtil.decrypt(null));
        assertNull(AESUtil.decrypt(""));
    }
}
