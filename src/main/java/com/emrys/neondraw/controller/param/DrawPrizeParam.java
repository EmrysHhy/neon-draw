package com.emrys.neondraw.controller.param;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 抽奖/开奖请求参数 一个活动可以有多个奖品，一个奖品可以有多个中奖者
 *
 * @author Emrys
 */
@Data
public class DrawPrizeParam implements Serializable {

    /**
     * 活动id
     */
    @NotNull(message = "活动id不能为空")
    private Long activityId;

    /**
     * 奖品id
     */
    @NotNull(message = "奖品id不能为空")
    private Long prizeId;

    /**
     * 中奖时间
     */
    @NotNull(message = "中奖时间不能为空")
    private Date winningTime;

    /**
     * 中奖者列表
     */
    @NotEmpty(message = "中奖者列表不能为空")
    @Valid
    private List<Winner> winnerList;

    @Data
    public static class Winner implements Serializable {

        /**
         * 中奖者id
         */
        @NotNull(message = "中奖者id不能为空")
        private Long userId;

        /**
         * 中奖者姓名
         */
        @NotBlank(message = "中奖者姓名不能为空")
        private String userName;
    }
}
