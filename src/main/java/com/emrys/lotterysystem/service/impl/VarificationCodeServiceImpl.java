package com.emrys.lotterysystem.service.impl;

import com.emrys.lotterysystem.common.errorcode.ServiceErrorCodeConstants;
import com.emrys.lotterysystem.common.exception.ServiceException;
import com.emrys.lotterysystem.common.utils.CaptchaUtil;
import com.emrys.lotterysystem.common.utils.EmailUtil;
import com.emrys.lotterysystem.common.utils.RedisUtil;
import com.emrys.lotterysystem.common.utils.RegexUtil;
import com.emrys.lotterysystem.service.VarificationCodeService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Emrys
 * content:
 */
@Service
public class VarificationCodeServiceImpl implements VarificationCodeService {
    //定义key的前缀
    private static final String KEY_PRE = "varification_code_";
    private static final Long SUSTAINABLE_TIM = 300L;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private EmailUtil emailUtil;

    /**
     * 生成验证码,并且发送至邮箱,并且缓存验证码
     *
     * @param email
     */
    @Override
    public void sendVarificationCode(String email) {
        //校验邮箱
        if (!RegexUtil.checkMail(email)) {
            throw new ServiceException(ServiceErrorCodeConstants.MAIL_ERROR);
        }
        //生成验证码,使用Hutool验证码生成器
        String captcha = CaptchaUtil.generateCaptcha();
        //发送验证码
        try {
            emailUtil.sendVerificationCode(email, captcha);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new ServiceException(ServiceErrorCodeConstants.MAIL_SEND_ERROR);
        }
        //缓存验证码
        redisUtil.set(KEY_PRE + email, captcha, SUSTAINABLE_TIM); // 设置过期时间为5分钟
    }

    /**
     * 获取验证码
     *
     * @param email
     * @return
     */
    @Override
    public String getVarificationCode(String email) {
        if (!RegexUtil.checkMail(email)) {
            throw new ServiceException(ServiceErrorCodeConstants.MAIL_ERROR);
        }
        return redisUtil.get(KEY_PRE + email);
    }
}
