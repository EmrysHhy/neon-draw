package com.emrys.lotterysystem.service.status.oprt;

import com.emrys.lotterysystem.common.errorcode.ServiceErrorCodeConstants;
import com.emrys.lotterysystem.common.exception.ServiceException;
import com.emrys.lotterysystem.dao.dataobject.ActivityPrizeDO;
import com.emrys.lotterysystem.dao.mapper.ActivityMapper;
import com.emrys.lotterysystem.dao.mapper.ActivityPrizeMapper;
import com.emrys.lotterysystem.service.dto.ConvertActivityStatusDTO;
import com.emrys.lotterysystem.service.enums.ActivityPrizeStatusEnum;
import com.emrys.lotterysystem.service.status.StatusOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Emrys
 * content:
 */
@Slf4j
@Component
public class ActivityStatusOperator extends StatusOperator {
    @Autowired
    ActivityPrizeMapper activityPrizeMapper;
    @Autowired
    ActivityMapper activityMapper;
    @Override
    public Integer sequence() {
        return 2;
    }

    @Override
    public Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        int notDrawCount = activityPrizeMapper.countByActivityIdAndStatus(
                convertActivityStatusDTO.getActivityId(),
                ActivityPrizeStatusEnum.NOT_DRAW.name()
        );
        if (notDrawCount > 0) {
            log.info("活动{}还有{}个奖品未抽取，暂不结束活动", convertActivityStatusDTO.getActivityId(), notDrawCount);
            return true;
        }
        try {
            int rows = activityMapper.updateStatusById(
                    convertActivityStatusDTO.getActivityId(),
                    convertActivityStatusDTO.getTargetActivityStatus().name()
            );
            return rows > 0;
        } catch (Exception e) {
            log.error("更新活动状态失败:", e);
            throw new ServiceException(ServiceErrorCodeConstants.STATUS_CONVERT_FAIL);
        }
    }
}
