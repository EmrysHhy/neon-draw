package com.emrys.neondraw.service.impl;

import com.emrys.neondraw.common.errorcode.ServiceErrorCodeConstants;
import com.emrys.neondraw.common.exception.ServiceException;
import com.emrys.neondraw.common.utils.RedisUtil;
import com.emrys.neondraw.controller.param.ActivityPrizeParam;
import com.emrys.neondraw.controller.param.ActivityUserParam;
import com.emrys.neondraw.controller.param.CreateActivityParam;
import com.emrys.neondraw.controller.param.PageParam;
import com.emrys.neondraw.dao.dataobject.ActivityDO;
import com.emrys.neondraw.dao.dataobject.ActivityPrizeDO;
import com.emrys.neondraw.dao.dataobject.ActivityUserDO;
import com.emrys.neondraw.dao.dataobject.PrizeDO;
import com.emrys.neondraw.dao.mapper.*;
import com.emrys.neondraw.service.ActivityService;
import com.emrys.neondraw.service.convert.ServiceConverter;
import com.emrys.neondraw.service.dto.ActivityDTO;
import com.emrys.neondraw.service.dto.ActivityDetailDTO;
import com.emrys.neondraw.service.dto.CreateActivityDTO;
import com.emrys.neondraw.service.dto.PageListDTO;
import com.emrys.neondraw.service.enums.ActivityStatusEnum;
import com.emrys.neondraw.service.enums.PrizeTierEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static com.emrys.neondraw.service.constants.RedisConstant.ACTIVITY_DETAIL_;

/**
 *
 * @author Emrys
 * content:
 */
@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    NormalUserMapper normalUserMapper;
    @Autowired
    PrizeMapper prizeMapper;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    ActivityUserMapper activityUserMapper;
    @Autowired
    ActivityPrizeMapper activityPrizeMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ObjectMapper objectMapper;


    /**
     * 活动最大持续时间
     */
    private static final Long ACTIVITY_TIMEOUT  = 60 * 60 * 24 * 3L; //活动缓存过期时间,3天
    /**
     * 创建活动
     * 多个数据库表的操作,加入spring事务,事务采用默认级别
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreateActivityDTO createActivity(CreateActivityParam param) {
        log.info("service:create activity, param: {}", param);
        //1.校验信息
        checkActivityInfo(param);
        //2.保存activity到数据库
        ActivityDO activityDo = new ActivityDO();

        activityDo.setActivityName(param.getActivityName());
        activityDo.setDescription(param.getDescription());
        activityDo.setStatus(ActivityStatusEnum.RUNNING.name());
        activityMapper.insert(activityDo);

        //3.保存activity-prize到数据库
        List<ActivityPrizeDO> activityPrizeDOS =
                ServiceConverter.convertToActivityPrizeDoList(param.getActivityPrizeList(),activityDo.getId());
        activityPrizeMapper.batchInsert(activityPrizeDOS);
        //4.保存activity-user到数据库
        List<ActivityUserDO> activityUserDOS =
                ServiceConverter.convertToActivityUserDoList(param.getActivityUserList(),activityDo.getId());
        activityUserMapper.batchInsert(activityUserDOS);
        //得到完整的活动信息ActivityDetailDTO,放入redis   以便于抽奖得到的结果显示
           //还需要获取单个奖品的name,url,price,desc信息
        List<Long> prizeIds = activityPrizeDOS.stream().map(ActivityPrizeDO::getPrizeId).distinct().toList();
        List<PrizeDO> prizeDOList = prizeMapper.getPrizeListByIds(prizeIds);
        ActivityDetailDTO activityDetailDTO =
                ServiceConverter.convertToActivityDetailDTO(activityDo, activityPrizeDOS, prizeDOList,activityUserDOS);
        //存放缓存
        cacheActivityDetail(activityDetailDTO);

        //返回
        CreateActivityDTO createActivityDTO = new CreateActivityDTO();
        createActivityDTO.setActivityId(activityDo.getId());
        return createActivityDTO;

    }

    /**
     * 获取活动的基本信息
     * @param param
     * @return
     */
    @Override
    public PageListDTO<ActivityDTO> findActivityList(PageParam param) {
        //1.获取活动总数
        int total = activityMapper.countActivity();
        //2.获取当前页面的活动列表
        List<ActivityDO> activityDOList = activityMapper.selectActivityList(param.getOffset(), param.getPageSize());
        List<ActivityDTO> activityDTOS = ServiceConverter.convertToActivityDTOList(activityDOList);
        return new PageListDTO<>(total, activityDTOS);
    }

    @Override
    public ActivityDetailDTO findActivityDetail(Long activityId) {
        if (activityId == null) {
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_ID_EMPTY);
        }
        ActivityDetailDTO activityDetailDTO = getActivityDetailDTO(activityId); //从缓存中获取
        if (activityDetailDTO == null || activityDetailDTO.getActivityId() == null) {
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_NOT_FOUND);
        }
        return activityDetailDTO;
    }

    /**
     * 将活动详情缓存到redis中
     * @param activityDetailDTO
     */
    @Override
    public void cacheActivityDetail(ActivityDetailDTO activityDetailDTO) {
        //key :   activity_detail_ + (#{id})
        //value : activityDetailDTO(json)

        //校验activityDetailDTO
        if(activityDetailDTO == null || activityDetailDTO.getActivityId() == null){
            log.error("cache activity detail failed, activityDetailDTO is null or activityId is null");
            //缓存失败不影响外部事务,不抛异常
            return;
        }
        try {

            redisUtil.set(ACTIVITY_DETAIL_ + activityDetailDTO.getActivityId(), objectMapper.writeValueAsString(activityDetailDTO), ACTIVITY_TIMEOUT);
        } catch (Exception e) {
            log.error("cache activity detail failed, activityDetailDTO is {}, error is {}", activityDetailDTO, e.getMessage());
            //不抛异常,缓存加入失败在后续可以再次加入缓存
        }

    }

    /**
     * 从redis中获取活动详情
     * @param activityId
     * @return
     */
    public ActivityDetailDTO getActivityDetailDTO(Long activityId) {
        String key = ACTIVITY_DETAIL_ + activityId;
        String value = redisUtil.get(key);
        try {
            if (value != null) {
                return objectMapper.readValue(value, ActivityDetailDTO.class); //成功从缓存中获取
            }
        } catch (Exception e) {
            log.error("get activity detail from redis failed, key is {}, error is {}", key, e.getMessage());
        }
        return loadActivityDetailFromDb(activityId);
    }

    /**
     * 从数据库获取细节
     * @param activityId
     * @return
     */
    @Override
    public ActivityDetailDTO loadActivityDetailFromDb(Long activityId) {
        log.info("无法从缓存中获取活动详情,从数据库中加载, activityId: {}", activityId);
        ActivityDetailDTO ret = queryActivityDetailFromDb(activityId);
        cacheActivityDetail(ret);
        return ret;
    }

    @Override
    public void refreshActivityDetailCache(Long activityId) {
        ActivityDetailDTO activityDetailDTO = queryActivityDetailFromDb(activityId);
        if (activityDetailDTO == null) {
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_NOT_FOUND);
        }
        putActivityDetailCacheStrict(activityDetailDTO);
    }

    private ActivityDetailDTO queryActivityDetailFromDb(Long activityId) {
        ActivityDO activityDo = activityMapper.selectById(activityId);
        if (activityDo == null) {
            return null;
        }
        List<ActivityPrizeDO> activityPrizeDOS = activityPrizeMapper.selectActivityPrizeDOListByActivityId(activityId);
        List<PrizeDO> prizeDOList = prizeMapper.selectPrizeListByActivityId(activityId);
        List<ActivityUserDO> activityUserDOS = activityUserMapper.selectActivityUserDOListByActivityId(activityId);
        return ServiceConverter.convertToActivityDetailDTO(
                activityDo, activityPrizeDOS, prizeDOList, activityUserDOS);
    }

    private void putActivityDetailCacheStrict(ActivityDetailDTO activityDetailDTO) {
        try {
            boolean success = redisUtil.set(
                    ACTIVITY_DETAIL_ + activityDetailDTO.getActivityId(),
                    objectMapper.writeValueAsString(activityDetailDTO),
                    ACTIVITY_TIMEOUT);
            if (!success) {
                throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_DETAIL_CACHE_ERROR);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("refresh activity detail cache failed, activityId: {}", activityDetailDTO.getActivityId(), e);
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_DETAIL_CACHE_ERROR);
        }
    }

    /**
     * 校验活动的参数是否有效
     * @param param
     */
    private void checkActivityInfo(CreateActivityParam param) {

        if(param == null){
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PARAM_EMPTY);
        }
        //校验人员id是否存在在数据库中
        List<Long> userIds = param.getActivityUserList().stream().map(ActivityUserParam::getUserId).distinct().toList();
        Long userNumbers = normalUserMapper.countByIds(userIds);
        if(userNumbers != userIds.size()){
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_USER_NOT_EXIST);

        }
        //校验奖品id是否存在在数据库中
        List<Long> prizeIds = param.getActivityPrizeList().stream().map(ActivityPrizeParam::getPrizeId).distinct().toList();
        Long PrizeNumbers = prizeMapper.countByIds(prizeIds);
        if(PrizeNumbers != prizeIds.size()){
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_NOT_EXIST);
        }

        //校验奖品数量应当 <= 参与人员
        if(PrizeNumbers > userNumbers){
            throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_TOO_MACH);
        }
        //校验奖品等级
        param.getActivityPrizeList().forEach(prize -> {
            if(PrizeTierEnum.forName(prize.getPrizeTiers()) == null){
                throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_TIER_ERROR);
            }
        });
    }
}
