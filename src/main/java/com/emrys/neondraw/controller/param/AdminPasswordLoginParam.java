package com.emrys.neondraw.controller.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
public class AdminPasswordLoginParam implements Serializable {
    @NotBlank(message = "邮箱或手机号不能为空")
    private String loginName;
    @NotBlank(message = "密码不能为空")
    @ToString.Exclude
    private String password;
}
