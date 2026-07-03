package com.emrys.lotterysystem.service.status.oprt;
import com.emrys.lotterysystem.common.errorcode.ServiceErrorCodeConstants;
import com.emrys.lotterysystem.common.exception.ServiceException;
import com.emrys.lotterysystem.dao.mapper.ActivityUserMapper;
import com.emrys.lotterysystem.service.dto.ConvertActivityStatusDTO;
import com.emrys.lotterysystem.service.status.StatusOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Emrys
 * content:人员状态转换
 */
@Component
@Slf4j
public class UserStatusOperator extends StatusOperator {

    @Autowired
    private ActivityUserMapper activityUserMapper;

    @Override
    public Integer sequence() {
        return 1;
    }

    @Override
    public Boolean convert(ConvertActivityStatusDTO convertActivityStatusDTO) {
        try {
            int rows = activityUserMapper.updateStatusByActivityIdAndUserIds(
                    convertActivityStatusDTO.getActivityId(),
                    convertActivityStatusDTO.getUserIds(),
                    convertActivityStatusDTO.getTargetUserStatus().name()
            );
            return rows > 0;
        } catch (Exception e) {
            log.error("更新人员状态失败:", e);
            throw new ServiceException(ServiceErrorCodeConstants.STATUS_CONVERT_FAIL);
        }
    }
}
