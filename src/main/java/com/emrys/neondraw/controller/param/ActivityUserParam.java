package com.emrys.neondraw.controller.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class ActivityUserParam implements Serializable {
    @NotNull(message = "活动id不能为空")
    private Long userId;//用户id
    @NotBlank(message = "活动id不能为空")
    private String userName;//用户姓名
}
