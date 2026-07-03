package com.emrys.lotterysystem.service.dto;

import com.emrys.lotterysystem.controller.result.FindPrizeListResult;
import com.emrys.lotterysystem.service.enums.ActivityPrizeStatusEnum;
import com.emrys.lotterysystem.service.enums.ActivityStatusEnum;
import com.emrys.lotterysystem.service.enums.ActivityUserStatusEnum;
import com.emrys.lotterysystem.service.enums.PrizeTierEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class ActivityDetailDTO {
    //活动信息
    private Long activityId;
    private String activityName;
    private String desc;
    private ActivityStatusEnum status;
    //奖品信息
    private List<PrizeDetail> prizeList;
    //人员信息
    private List<UserDetail> userList;

    public Boolean validActivity(){
        return ActivityStatusEnum.RUNNING.equals(this.status);
    }
    @Data
    public static class PrizeDetail {
        private Long prizeId;
        private String prizeName;
        private String imageUrl;
        private BigDecimal price;
        private String description;
        private Long prizeAmount; //奖品数量
        private PrizeTierEnum tiers; //奖品等级
        private String prizeTierName; //奖品等级名称，如：一等奖
        private ActivityPrizeStatusEnum status; //奖品状态
        //还未被抽出
        public Boolean validPrize(){
            return ActivityPrizeStatusEnum.NOT_DRAW.equals(this.status) && this.prizeAmount > 0;
        }
    }
    @Data
    public static class UserDetail {
        private Long userId;
        private String userName;
        private ActivityUserStatusEnum status; //是否中奖
        //还未中奖
        public Boolean validUser(){
            return ActivityUserStatusEnum.NOT_WIN.equals(this.status);
        }
    }
}
