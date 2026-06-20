package com.emrys.lotterysystem.controller.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class UserRegisterParam implements Serializable {
    @NotBlank(message = "用户名不能为空")
    private String name;//姓名
    @NotBlank(message = "邮箱不能为空")
    private String mail;//邮箱
    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;//手机号2
    @ToString.Exclude
    private String password;//密码
    @NotBlank(message = "身份信息不能为空")
    private String identity;//身份信息
}
