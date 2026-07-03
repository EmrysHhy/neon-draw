package com.emrys.lotterysystem.service;

/**
 * 验证码服务接口。
 * <p>
 * 负责向指定邮箱发送登录/注册验证码，并将验证码暂存到 Redis 供后续校验使用。
 *
 * @author Emrys
 */
public interface VarificationCodeService {
    /**
     * 发送验证码
     * @param email
     */
    void sendVarificationCode(String email);

    /**
     * 获取验证码
     * @param email
     * @return
     */
    String getVarificationCode(String email);
}
