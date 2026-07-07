package com.emrys.lotterysystem.controller.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
public class AdminRegisterParam implements Serializable {
    @NotBlank(message = "用户名不能为空")
    private String name;
    @NotBlank(message = "邮箱不能为空")
    private String mail;
    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;
    @NotBlank(message = "密码不能为空")
    @ToString.Exclude
    private String password;
}
