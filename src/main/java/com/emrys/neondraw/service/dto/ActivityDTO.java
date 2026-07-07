package com.emrys.neondraw.service.dto;

import com.emrys.neondraw.service.enums.ActivityStatusEnum;
import lombok.Data;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class ActivityDTO {
    private Long activityId;
    private String activityName;
    private String description;
    private ActivityStatusEnum status;  //活动状态

    /**
     * 活动当前是否还在进行?
     * @return
     */
    public Boolean valid(){
        return ActivityStatusEnum.RUNNING.equals(status);
    }
}
