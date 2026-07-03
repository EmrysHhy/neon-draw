package com.emrys.lotterysystem.service.dto;

import com.emrys.lotterysystem.service.enums.ActivityPrizeStatusEnum;
import com.emrys.lotterysystem.service.enums.ActivityStatusEnum;
import com.emrys.lotterysystem.service.enums.ActivityUserStatusEnum;
import lombok.Data;

import java.util.List;

/**
 *
 * @author Emrys
 * content:处理状态转换
 */
@Data
public class ConvertActivityStatusDTO {
    private Long activityId;
    private ActivityStatusEnum targetActivityStatus;  //想要转换的目标状态
    private Long prizeId;
    private ActivityPrizeStatusEnum targetPrizeStatus;  //想要转换的目标状态
    private List<Long> userIds;
    private ActivityUserStatusEnum targetUserStatus;  //想要转换的目标状态
}
