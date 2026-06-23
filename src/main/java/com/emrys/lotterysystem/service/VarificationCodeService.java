package com.emrys.lotterysystem.service;

/**
 *
 * @author Emrys
 * content:
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
