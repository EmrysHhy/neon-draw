package com.emrys.neondraw.common.utils;

import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;

/**
 *
 * @author Emrys
 * content:
 */
public class CaptchaUtil {
    private final static Integer CAPTCHA_LEN = 6; // 验证码长度
    public static String generateCaptcha() {
        // 生成一个随机的4位数字验证码
        // 自定义纯数字的验证码（随机4位数字，可重复）
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", CAPTCHA_LEN);
        LineCaptcha lineCaptcha = cn.hutool.captcha.CaptchaUtil.createLineCaptcha(200, 100);
        lineCaptcha.setGenerator(randomGenerator);
        // 重新生成code
        lineCaptcha.createCode();
        return lineCaptcha.getCode();
    }

    /**
     * 自定义长度验证码
     * @param len
     * @return
     */
    public static String generateCaptcha(int len) {
        // 生成一个随机的4位数字验证码
        // 自定义纯数字的验证码（随机4位数字，可重复）
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", len);
        LineCaptcha lineCaptcha = cn.hutool.captcha.CaptchaUtil.createLineCaptcha(200, 100);
        lineCaptcha.setGenerator(randomGenerator);
        // 重新生成code
        lineCaptcha.createCode();
        return lineCaptcha.getCode();
    }

}
