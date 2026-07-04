package com.emrys.lotterysystem.service;

import com.emrys.lotterysystem.controller.param.DrawPrizeParam;
import com.emrys.lotterysystem.controller.param.ShowWinningRecordsParam;
import com.emrys.lotterysystem.dao.dataobject.WinningRecordDO;
import com.emrys.lotterysystem.service.dto.ActivityDetailDTO;
import com.emrys.lotterysystem.service.dto.ShowWinningRecordsDTO;

import java.util.List;

/**
 * 抽奖业务服务接口。
 * <p>
 * 负责抽奖请求的发送（写入 RabbitMQ）、抽奖参数校验，以及中奖后的状态转换触发。
 * 完整消费流程：校验参数 → 转换活动/奖品/人员状态 → 持久化中奖记录。
 *
 * @author Emrys
 */
public interface DrawPrizeService {
    /**
     * 发送抽奖请求到消息队列。
     */
    void drawPrize(DrawPrizeParam param);

    /**
     * MQ 消费端：校验、改状态、存中奖记录（同一事务，失败自动回滚 DB）。
     */
    List<WinningRecordDO> processDrawPrize(DrawPrizeParam param);

    /**
     * 事务成功后刷新 Redis 缓存；缓存中途失败则回滚本次已写入的 key，不影响 DB。
     */
    void refreshCacheAfterDraw(DrawPrizeParam param, List<WinningRecordDO> winningRecordList);

    /**
     * 校验抽奖参数（活动、奖品、中奖人员是否合法）。
     */
    void checkDrowPrizeParam(DrawPrizeParam param);

    /**
     * 中奖后触发活动/奖品/人员状态转换。
     */
    void statusConvert(DrawPrizeParam param);

    /**
     * 保存中奖者名单到数据库
     */
    List<WinningRecordDO> saveWinnerRecords(DrawPrizeParam param);
    /**
     * 将中奖记录写入 Redis 缓存。
     * 奖品维度：奖品抽取后即可调用；活动维度：需等活动全部奖品抽取完毕。
     */
    void cacheWinnerRecord(String key, List<WinningRecordDO> winningRecordList, Long timeout);


    /**
     * 从 Redis 读取中奖记录。
     */
    List<WinningRecordDO> getWinningRecordsFromCache(String key);

    /**
     * 从数据库加载中奖记录（活动维度），并写入缓存。
     */
    List<WinningRecordDO> loadWinningRecordsFromDbByActivityId(Long activityId);

    /**
     * 从数据库加载中奖记录（奖品维度），并写入缓存。
     */
    List<WinningRecordDO> loadWinningRecordsFromDbByActivityIdAndPrizeId(Long activityId, Long prizeId);

    /**
     * 展示中奖列表
     * @param showWinningRecordsParam
     * @return
     */
    List<ShowWinningRecordsDTO> showWinningRecords(ShowWinningRecordsParam showWinningRecordsParam);
}
