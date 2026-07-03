package com.emrys.lotterysystem.service;

import com.emrys.lotterysystem.controller.param.CreateActivityParam;
import com.emrys.lotterysystem.controller.param.PageParam;
import com.emrys.lotterysystem.service.dto.ActivityDTO;
import com.emrys.lotterysystem.service.dto.ActivityDetailDTO;
import com.emrys.lotterysystem.service.dto.CreateActivityDTO;
import com.emrys.lotterysystem.service.dto.PageListDTO;

/**
 * 活动业务服务接口。
 * <p>
 * 负责活动的创建、分页查询、详情获取，以及活动详情在 Redis 中的缓存读写。
 * 抽奖页面展示的活动/奖品/人员信息主要通过 {@link #findActivityDetail(Long)} 从缓存获取。
 *
 * @author Emrys
 */
public interface ActivityService {
    /**
     * 创建活动，并写入活动、奖品、人员关联数据及活动详情缓存。
     */
    CreateActivityDTO createActivity(CreateActivityParam param);

    /**
     * 分页查询活动列表。
     */
    PageListDTO<ActivityDTO> findActivityList(PageParam param);

    /**
     * 从 Redis 缓存读取活动详情；缓存不存在时回源数据库并回填缓存。
     */
    ActivityDetailDTO findActivityDetail(Long activityId);

    /**
     * 将活动详情写入 Redis 缓存。
     */
    void cacheActivityDetail(ActivityDetailDTO activityDetailDTO);

    /**
     * 从数据库加载活动详情（含奖品、人员），并同步写入缓存。
     */
    ActivityDetailDTO loadActivityDetailFromDb(Long activityId);

    /**
     * 抽奖后刷新活动详情缓存，写入失败抛异常（供缓存回滚流程使用）。
     */
    void refreshActivityDetailCache(Long activityId);
}
