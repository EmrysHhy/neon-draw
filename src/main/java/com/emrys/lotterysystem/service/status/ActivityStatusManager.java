package com.emrys.lotterysystem.service.status;

import com.emrys.lotterysystem.service.dto.ConvertActivityStatusDTO;

/**
 * 活动状态转换管理器接口。
 * <p>
 * 负责协调各 {@link com.emrys.lotterysystem.service.status.StatusOperator} 按顺序执行状态转换：
 * 先更新奖品/人员状态，再判断是否需要将活动标记为已结束，最后刷新活动详情缓存。
 *
 * @author Emrys
 */
public interface ActivityStatusManager {
    /**
     * 处理一次状态转换事件（通常由抽奖成功后触发）。
     */
    void handleEvent(ConvertActivityStatusDTO convertActivityStatusDTO);
}
