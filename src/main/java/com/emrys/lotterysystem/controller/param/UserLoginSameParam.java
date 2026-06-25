package com.emrys.lotterysystem.controller.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Emrys
 * content:登录中的同一参数
 */
@Data
public class UserLoginSameParam implements Serializable {
    @NotBlank(message = "身份信息不能为空")
    private  String mandatoryIdentity;
}
