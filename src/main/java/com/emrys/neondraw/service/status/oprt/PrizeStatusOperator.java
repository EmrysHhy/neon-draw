package com.emrys.neondraw.service.status.oprt;

import com.emrys.neondraw.common.errorcode.ServiceErrorCodeConstants;
import com.emrys.neondraw.common.exception.ServiceException;
import com.emrys.neondraw.dao.mapper.ActivityPrizeMapper;
import com.emrys.neondraw.service.dto.ConvertActivityStatusDTO;
import com.emrys.neondraw.service.status.StatusOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Emrys
 * content:奖品状态转换
 */
@Component
@Slf4j
public class PrizeStatusOperator extends StatusOperator {

    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Override
    public Integer sequence() {
        return 1;
    }

    @Override
    public Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        try {
            int rows = activityPrizeMapper.updateStatusByActivityIdAndPrizeId(
                    convertActivityStatusDTO.getActivityId(),
                    convertActivityStatusDTO.getPrizeId(),
                    convertActivityStatusDTO.getTargetPrizeStatus().name()
            );
            return rows > 0;
        } catch (Exception e) {
            log.error("更新奖品状态失败:", e);
            throw new ServiceException(ServiceErrorCodeConstants.STATUS_CONVERT_FAIL);
        }
    }
}
