package com.emrys.neondraw.controller.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author Emrys
 * content:展示中奖列表
 */
@Data
public class ShowWinningRecordsParam implements Serializable {
    @NotNull(message = "活动id不能为空")
    private Long activityId;
    private Long prizeId; //如果[prizeId]为空，则查询该活动下所有奖品的中奖记录
}
