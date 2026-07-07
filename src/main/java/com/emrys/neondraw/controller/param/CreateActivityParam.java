package com.emrys.neondraw.controller.param;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class CreateActivityParam implements Serializable {
    @NotBlank(message = "活动名称不能为空")
    private String activityName;
    private String description;
    @NotEmpty(message = "奖品列表不能为空")
    @Valid
    private List<ActivityPrizeParam> activityPrizeList;
    @NotEmpty(message = "参与用户列表不能为空")
    @Valid
    private List<ActivityUserParam> activityUserList;
}
