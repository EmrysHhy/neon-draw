package com.emrys.lotterysystem.controller.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *
 * @author Emrys
 * content:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVarificationLoginParam  extends UserLoginSameParam implements Serializable {
    @JsonProperty("loginMobile")
    @NotBlank(message = "邮箱不能为空")
    private String email;
    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空！")
    private String verificationCode;
}
