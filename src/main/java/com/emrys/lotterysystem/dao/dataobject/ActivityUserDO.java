package com.emrys.lotterysystem.dao.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Emrys
 * content:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityUserDO extends BaseDO{
    private Long activityId;
    private Long userId;
    private String userName;
    private String status; //是否中奖
}
