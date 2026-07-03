package com.emrys.lotterysystem.controller.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class ActivityDetailResult implements Serializable {
    //活动信息
    private Long activityId;
    private String activityName;
    private String desc;
    private Boolean valid;
    //奖品信息
    private List<PrizeDetail> prizes;
    //人员信息
    private List<UserDetail> users;


    @Data
    public static class PrizeDetail {
        private Long prizeId;
        private String name;
        private String prizeTierName; // 奖品等级 message，如：一等奖
        private String imageUrl;
        private Long prizeAmount;
        private Boolean valid;
    }

    @Data
    public static class UserDetail {
        private Long userId;
        private String userName;
        private Boolean valid; // 是否还可参与抽奖（未中奖）
    }
}
