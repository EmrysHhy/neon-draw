package com.emrys.neondraw.controller.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class FindActivityListResult implements Serializable {
    private Integer total;//总奖品数量
    private List<FindActivityListResult.ActivityInfo> records;//奖品列表

    /**
     * 活动基本信息
     */
    @Data
    public static class ActivityInfo implements Serializable {
        private Long activityId;
        private String activityName;
        private String description;
        private Boolean valid;  //活动状态
    }
}
