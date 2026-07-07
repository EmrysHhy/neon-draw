package com.emrys.neondraw.dao.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Emrys
 * content:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityPrizeDO extends BaseDO{
    private Long activityId;
    private Long prizeId;
    private Integer prizeAmount; //奖品数量
    private String prizeTiers; //奖品等级
    private String status; //奖品状态
}
