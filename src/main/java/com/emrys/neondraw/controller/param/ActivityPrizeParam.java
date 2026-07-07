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
public class ActivityPrizeParam implements Serializable {
    @NotNull(message = "活动ID不能为空")
    private Long prizeId;//奖品ID
    @NotNull(message = "奖品数量不能为空")
    private  Long prizeAmount;//奖品数量
    @NotBlank(message = "奖品等级不能为空")
    private String prizeTiers;//奖品等级
}
