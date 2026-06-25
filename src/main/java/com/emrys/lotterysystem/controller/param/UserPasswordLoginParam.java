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
public class UserPasswordLoginParam extends UserLoginSameParam implements Serializable {
    @NotBlank(message = "邮箱或手机号不能为空")
    private String loginName;//可以是手机也可以是邮箱
    private String password;
}
