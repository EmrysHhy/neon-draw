package com.emrys.neondraw.service.impl;

import com.emrys.neondraw.common.errorcode.ServiceErrorCodeConstants;
import com.emrys.neondraw.common.exception.ServiceException;
import com.emrys.neondraw.common.utils.RedisUtil;
import com.emrys.neondraw.controller.param.DrawPrizeParam;

import com.emrys.neondraw.controller.param.ShowWinningRecordsParam;
import com.emrys.neondraw.dao.dataobject.*;
import com.emrys.neondraw.dao.mapper.*;
import com.emrys.neondraw.service.DrawPrizeService;
import com.emrys.neondraw.service.ActivityService;
import com.emrys.neondraw.service.convert.ServiceConverter;
import com.emrys.neondraw.service.dto.ShowWinningRecordsDTO;
import com.emrys.neondraw.service.status.ActivityStatusManager;
import com.emrys.neondraw.service.dto.ConvertActivityStatusDTO;
import com.emrys.neondraw.service.enums.ActivityPrizeStatusEnum;
import com.emrys.neondraw.service.enums.ActivityStatusEnum;
import com.emrys.neondraw.service.enums.ActivityUserStatusEnum;
import com.emrys.neondraw.service.constants.MQConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.emrys.neondraw.common.config.DirectRabbitConfig.EXCHANGE_NAME;
import static com.emrys.neondraw.common.config.DirectRabbitConfig.ROUTING;
import static com.emrys.neondraw.service.constants.RedisConstant.*;

/**
 *
 * @author Emrys
 * content:
 */
@Service
@Slf4j
public class DrawPrizeServiceImpl implements DrawPrizeService {
    @Autowired
    ActivityStatusManager activityStatusManager;
    @Autowired
    ActivityService activityService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    ActivityPrizeMapper activityPrizeMapper;
    @Autowired
    ActivityUserMapper activityUserMapper;
    @Autowired
    PrizeMapper prizeMapper;
    @Autowired
    NormalUserMapper normalUserMapper;
    @Autowired
    WinningRecordMapper winningRecordMapper;
    @Autowired
    RedisUtil redis;
    @Autowired
    ObjectMapper objectMapper;

    /**
     * 抽奖：入口校验 + 防重复锁，通过后发送到消息队列。
     */
    @Override
    public void drawPrize(DrawPrizeParam param) {
        log.info("drawPrize param:{}", param);
        String lockKey = acquireDrawPrizeLock(param);
        try {
            Map<String, Object> map = new HashMap<>();
            map.put(MQConstant.MESSAGE_ID, UUID.randomUUID().toString());
            map.put(MQConstant.MESSAGE_DATA, param);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING, map);
            log.info("drawPrize send to rabbitmq success, param:{}", param);
        } catch (Exception e) {
            redis.delete(lockKey);
            throw e;
        }
    }

    /**
     * 入队前校验：奖品未抽取 + Redis 防重复提交锁。
     */
    private String acquireDrawPrizeLock(DrawPrizeParam param) {
        ActivityPrizeDO activityPrizeDO = activityPrizeMapper.selectByActivityIdAndPrizeId(
                param.getActivityId(), param.getPrizeId());
        if (activityPrizeDO == null) {
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_NOT_EXIST);
        }
        if (!ActivityPrizeStatusEnum.NOT_DRAW.name().equals(activityPrizeDO.getStatus())) {
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_ALREADY_DRAW);
        }
        String lockKey = DRAW_PRIZE_LOCK_ + param.getActivityId() + "_" + param.getPrizeId();
        if (!redis.setIfAbsent(lockKey, "1", DRAW_PRIZE_LOCK_TIMEOUT)) {
            throw new ServiceException(ServiceErrorCodeConstants.DRAW_PRIZE_DUPLICATE);
        }
        return lockKey;
    }

    /**
     * MQ 消费端核心流程：仅 DB 操作在同一事务内。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<WinningRecordDO> processDrawPrize(DrawPrizeParam param) {
        checkDrowPrizeParam(param); //校验参数
        statusConvert(param);    //状态转换
        return saveWinnerRecords(param);
    }

    /**
     * 事务提交后刷新缓存；任一步失败则删除本次已写入的 key，不回滚 DB。
     */
    @Override
    public void refreshCacheAfterDraw(DrawPrizeParam param, List<WinningRecordDO> winningRecordList) {
        if (param == null || winningRecordList == null || winningRecordList.isEmpty()) {
            return;
        }
        Long activityId = param.getActivityId();
        Long prizeId = param.getPrizeId();
        List<String> cachedKeys = new ArrayList<>();

        try {
            activityService.refreshActivityDetailCache(activityId);
            cachedKeys.add(ACTIVITY_DETAIL_ + activityId);

            String prizeRecordKey = WINNING_ACTIVITY_PRIZE_RECORD_ + activityId + "_" + prizeId;
            cacheWinnerRecordStrict(prizeRecordKey, winningRecordList, WINNING_TIMEOUT);
            cachedKeys.add(prizeRecordKey);

            if (isActivityComplete(activityId)) {
                String activityRecordKey = WINNING_ACTIVITY_RECORD_ + activityId;
                List<WinningRecordDO> allList = winningRecordMapper.selectByActivityId(activityId);
                cacheWinnerRecordStrict(activityRecordKey, allList, WINNING_TIMEOUT);
                cachedKeys.add(activityRecordKey);
            }
        } catch (Exception e) {
            rollbackCacheKeys(cachedKeys);
            log.error("刷新缓存失败，已回滚本次写入的缓存, activityId:{}", activityId, e);
        }
    }
/**
 * 对List<WinningRecordDO> winningRecordList缓存
 * @param key
 * @param winningRecordList
 * @param timeout
 */
    private void cacheWinnerRecordStrict(String key, List<WinningRecordDO> winningRecordList, Long timeout) {
        try {
            String value = objectMapper.writeValueAsString(winningRecordList);
            if (!redis.set(key, value, timeout)) {
                throw new ServiceException(ServiceErrorCodeConstants.WINNING_RECORD_CACHE_ERROR);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("缓存中奖记录失败, key:{}", key, e);
            throw new ServiceException(ServiceErrorCodeConstants.WINNING_RECORD_CACHE_ERROR);
        }
    }
/**
 * 回滚缓存
 * @param keys
 */
    private void rollbackCacheKeys(List<String> keys) {
        if (keys.isEmpty()) {
            return;
        }
        redis.delete(keys.toArray(new String[0]));
        log.warn("已回滚缓存 keys: {}", keys);
    }

    /**
     * 校验从消息队列得到的参数
     * @param param
     */
    @Override
    public void checkDrowPrizeParam(DrawPrizeParam param) {
        //1.判断活动是否存在
        ActivityDO activityDO = activityMapper.selectById(param.getActivityId());
        if (activityDO == null) {
            log.error("活动不存在, activityId:{}", param.getActivityId());
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_NOT_FOUND);
        }
        //2.判断活动当前是否有效
        if(!ActivityStatusEnum.RUNNING.name().equals(activityDO.getStatus())){
            log.error("活动当前不在运行状态, activityId:{}, status:{}", param.getActivityId(), activityDO.getStatus());
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_NOT_RUNNING);
        }
        //3.奖品是否存在活动中，以及奖品是否为未抽取状态
        ActivityPrizeDO activityPrizeDO = activityPrizeMapper.selectByActivityIdAndPrizeId(param.getActivityId(), param.getPrizeId());
        if(activityPrizeDO == null){
            log.error("奖品不存在于活动中, activityId:{}, prizeId:{}", param.getActivityId(), param.getPrizeId());
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_NOT_EXIST);
        }
        if (!ActivityPrizeStatusEnum.NOT_DRAW.name().equals(activityPrizeDO.getStatus())) {
            log.error("奖品已被抽取, activityId:{}, prizeId:{}, status:{}", param.getActivityId(), param.getPrizeId(), activityPrizeDO.getStatus());
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_ALREADY_DRAW);
        }
        //4.中奖人员是否都在活动参与人员中
        List<Long> userIds = param.getWinnerList().stream()
                .map(DrawPrizeParam.Winner::getUserId)
                .distinct()
                .toList();
        List<ActivityUserDO> activityUserList = activityUserMapper.selectByActivityIdAndUserIds(param.getActivityId(), userIds);
        if (activityUserList.size() != userIds.size()) {
            log.error("中奖人员不在活动参与人员中, activityId:{}, userIds:{}", param.getActivityId(), userIds);
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_WINNER_NOT_IN_ACTIVITY);
        }
        //5.中奖人员是否处于有效状态（未中奖）
        boolean allNotWin = activityUserList.stream()
                .allMatch(user -> ActivityUserStatusEnum.NOT_WIN.name().equals(user.getStatus()));
        if (!allNotWin) {
            log.error("中奖人员状态无效, activityId:{}, userIds:{}", param.getActivityId(), userIds);
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_WINNER_INVALID_STATUS);
        }
        //6.奖品数量是否与中奖人员数量一致
        if (activityPrizeDO.getPrizeAmount() != param.getWinnerList().size()) {
            log.error("奖品数量与中奖人员数量不一致, activityId:{}, prizeId:{}, prizeAmount:{}, winnerSize:{}",
                    param.getActivityId(), param.getPrizeId(), activityPrizeDO.getPrizeAmount(), param.getWinnerList().size());
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_AMOUNT_EXCEED_WINNER);
        }

    }

    /**
     * 中奖之后状态转化
     *///转化奖品状态`
       //转化用户状态
       //是否转换活动状态?
       //更新完成的活动信息
    @Override
    public void statusConvert(DrawPrizeParam param) {
        //定义转换对象
        ConvertActivityStatusDTO convertActivityStatusDTO = new ConvertActivityStatusDTO();
        convertActivityStatusDTO.setActivityId(param.getActivityId());
        convertActivityStatusDTO.setTargetActivityStatus(ActivityStatusEnum.COMPLETE);
        convertActivityStatusDTO.setPrizeId(param.getPrizeId());
        convertActivityStatusDTO.setTargetPrizeStatus(ActivityPrizeStatusEnum.DRAWN);
        convertActivityStatusDTO.setUserIds(
                param.getWinnerList()
                        .stream()
                        .map(DrawPrizeParam.Winner::getUserId)
                        .distinct()
                        .toList());
        convertActivityStatusDTO.setTargetUserStatus(ActivityUserStatusEnum.WIN);


        //让处理器处理
        activityStatusManager.handleEvent(convertActivityStatusDTO);

    }

    /**
     * 保存中奖者名单
     */
    @Override
    public List<WinningRecordDO> saveWinnerRecords(DrawPrizeParam param) {
        //1.查询相关数据,活动表,奖品表,人员表,活动奖品关联
        ActivityDO activityDO = activityMapper.selectById(param.getActivityId());
        PrizeDO prizeDO = prizeMapper.selectById(param.getPrizeId());
        ActivityPrizeDO activityPrizeDO = activityPrizeMapper.selectByActivityIdAndPrizeId(param.getActivityId(), param.getPrizeId());
        List<NormalUserDO> userList = normalUserMapper.selectByIds(
                param.getWinnerList().stream()
                        .map(DrawPrizeParam.Winner::getUserId)
                        .distinct()
                        .toList()
        );

        //2.构造中奖记录（每位中奖者一条）
        List<WinningRecordDO> winningRecordList = new ArrayList<>();
        for (DrawPrizeParam.Winner winner : param.getWinnerList()) {
            NormalUserDO user = userList.stream()
                    .filter(u -> u.getId().equals(winner.getUserId()))
                    .findFirst()
                    .orElseThrow(() -> new ServiceException(ServiceErrorCodeConstants.USER_NOT_FOUND));

            WinningRecordDO winningRecordDO = new WinningRecordDO();
            winningRecordDO.setActivityId(activityDO.getId());
            winningRecordDO.setActivityName(activityDO.getActivityName());
            winningRecordDO.setPrizeId(prizeDO.getId());
            winningRecordDO.setPrizeName(prizeDO.getName());
            winningRecordDO.setPrizeTier(activityPrizeDO.getPrizeTiers());
            winningRecordDO.setWinnerId(winner.getUserId());
            winningRecordDO.setWinnerName(winner.getUserName());
            winningRecordDO.setWinnerEmail(user.getEmail());
            winningRecordDO.setWinnerPhoneNumber(user.getPhoneNumber());
            winningRecordDO.setWinningTime(param.getWinningTime());
            winningRecordList.add(winningRecordDO);
        }
        winningRecordMapper.batchInsert(winningRecordList);
        return winningRecordList;
    }

    /**
     * 判断活动是否已结束（所有奖品均已抽取）
     */
    private boolean isActivityComplete(Long activityId) {
        ActivityDO activityDO = activityMapper.selectById(activityId);
        return activityDO != null && ActivityStatusEnum.COMPLETE.name().equals(activityDO.getStatus());
    }


    /**
     * 存入缓存
     * @param
     */
    @Override
    public void cacheWinnerRecord(String key, List<WinningRecordDO> winningRecordList, Long timeout) {
        try {
            String value = objectMapper.writeValueAsString(winningRecordList);
            redis.set(key, value, timeout);
        } catch (Exception e) {
            log.error("缓存中奖记录失败, key:{}, error:{}", key, e.getMessage());
        }
    }


    /**
     * 从 Redis 读取中奖记录
     */
    @Override
    public List<WinningRecordDO> getWinningRecordsFromCache(String key) {
        String value = redis.get(key);
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.readValue(value, objectMapper.getTypeFactory().constructCollectionType(List.class, WinningRecordDO.class));
        } catch (Exception e) {
            log.error("从Redis获取中奖记录失败, key:{}, error:{}", key, e.getMessage());
            return null;
        }
    }

    /**
     * 从数据库加载活动维度中奖记录，并在活动已结束时回填 Redis。
     *
     * @param activityId 活动id
     * @return 该活动下所有中奖记录
     */
    @Override
    public List<WinningRecordDO> loadWinningRecordsFromDbByActivityId(Long activityId) {
        log.info("无法从缓存获取中奖记录, 从数据库加载, activityId:{}", activityId);
        List<WinningRecordDO> records = winningRecordMapper.selectByActivityId(activityId);
        // 活动全部奖品抽取完毕才写入活动维度缓存
        if (!records.isEmpty() && isActivityComplete(activityId)) {
            cacheWinnerRecord(WINNING_ACTIVITY_RECORD_ + activityId, records, WINNING_TIMEOUT);
        }
        return records;
    }

    /**
     * 从数据库加载奖品维度中奖记录，并在有记录时回填 Redis。
     *
     * @param activityId 活动id
     * @param prizeId    奖品id
     * @return 该活动下指定奖品的中奖记录
     */
    @Override
    public List<WinningRecordDO> loadWinningRecordsFromDbByActivityIdAndPrizeId(Long activityId, Long prizeId) {
        log.info("无法从缓存获取中奖记录, 从数据库加载, activityId:{}, prizeId:{}", activityId, prizeId);
        List<WinningRecordDO> records = winningRecordMapper.selectByActivityIdAndPrizeId(activityId, prizeId);
        // 该奖品已有中奖记录即可写入奖品维度缓存
        if (!records.isEmpty()) {
            cacheWinnerRecord(WINNING_ACTIVITY_PRIZE_RECORD_ + activityId + "_" + prizeId, records, WINNING_TIMEOUT);
        }
        return records;
    }

    /**
     * 展示中奖记录
     * @param param
     * @return
     */
    @Override
    public List<ShowWinningRecordsDTO> showWinningRecords(ShowWinningRecordsParam param) {
        //校验参数
        if(param.getActivityId() == null){
            log.error("活动id为空, param:{}", param);
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_ID_EMPTY);
        }
        //查询返回
        String key = "";
        List<WinningRecordDO> records = null;
        if (param.getPrizeId() == null){ //查询活动维度
             key = WINNING_ACTIVITY_RECORD_ + param.getActivityId();
            // 1. 先从 Redis 读取
            records = getWinningRecordsFromCache(key);
            if (records != null) {
                return ServiceConverter.convertToShowWinningRecordsDTOList(records);
            }
            // 2. 缓存未命中，从数据库加载
            records = loadWinningRecordsFromDbByActivityId(param.getActivityId());

        }else{
            //查询奖品维度下的中奖记录
            //1.查缓存
            key = WINNING_ACTIVITY_RECORD_ + param.getActivityId()+"_"+param.getPrizeId();
            records = getWinningRecordsFromCache(key);
            if (records != null) {
                //在缓存中,返回
                return ServiceConverter.convertToShowWinningRecordsDTOList(records);
            }
            //2.缓存查不到;->查数据库
            records = loadWinningRecordsFromDbByActivityIdAndPrizeId(param.getActivityId(), param.getPrizeId());
        }
        //3.重进加入缓存
        try {
            redis.setIfAbsent(key, objectMapper.writeValueAsString(records), WINNING_TIMEOUT);
        } catch (Exception e) {
            log.error("缓存中奖记录失败, key:{}, error:{}", key, e.getMessage());
        }
        return ServiceConverter.convertToShowWinningRecordsDTOList(records);
    }
}
