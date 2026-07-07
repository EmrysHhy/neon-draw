package com.emrys.neondraw.controller.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class AdminVerificationLoginParam implements Serializable {
    @JsonProperty("loginMobile")
    @NotBlank(message = "邮箱不能为空")
    private String email;
    @NotBlank(message = "验证码不能为空！")
    private String verificationCode;
}
