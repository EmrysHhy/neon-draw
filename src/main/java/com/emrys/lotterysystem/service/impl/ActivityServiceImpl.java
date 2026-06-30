package com.emrys.lotterysystem.service.impl;
import java.time.LocalDateTime;

import com.emrys.lotterysystem.common.errorcode.ServiceErrorCodeConstants;
import com.emrys.lotterysystem.common.exception.ServiceException;
import com.emrys.lotterysystem.controller.UserController;
import com.emrys.lotterysystem.controller.param.ActivityPrizeParam;
import com.emrys.lotterysystem.controller.param.ActivityUserParam;
import com.emrys.lotterysystem.controller.param.CreateActivityParam;
import com.emrys.lotterysystem.dao.dataobject.ActivityDo;
import com.emrys.lotterysystem.dao.dataobject.ActivityPrizeDO;
import com.emrys.lotterysystem.dao.dataobject.ActivityUserDO;
import com.emrys.lotterysystem.dao.mapper.*;
import com.emrys.lotterysystem.service.ActivityService;
import com.emrys.lotterysystem.service.convert.ServiceConverter;
import com.emrys.lotterysystem.service.dto.CreateActivityDTO;
import com.emrys.lotterysystem.service.enums.ActivityPrizeStatusEnum;
import com.emrys.lotterysystem.service.enums.ActivityStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    PrizeMapper prizeMapper;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    ActivityUserMapper activityUserMapper;
    @Autowired
    ActivityPrizeMapper activityPrizeMapper;
    /**
     * 创建活动
     * 多个数据库表的操作,加入spring事务,事务采用默认级别
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreateActivityDTO createActivity(CreateActivityParam param) {

        //1.校验信息
        checkActivityInfo(param);
        //2.保存activity到数据库
        ActivityDo activityDo = new ActivityDo();

        activityDo.setActivityName(param.getActivityName());
        activityDo.setDescription(param.getDescription());
        activityDo.setStatus(ActivityStatusEnum.RUNNING.name());
        activityMapper.insert(activityDo);

        //3.保存activity-prize到数据库
        List<ActivityPrizeDO> activityPrizeDOS = ServiceConverter.convertToActivityPrizeDoList(param.getActivityPrizeList(),activityDo.getId());
        activityPrizeMapper.batchInsert(activityPrizeDOS);
        //4.保存activity-user到数据库
        List<ActivityUserDO> activityUserDOS = ServiceConverter.convertToActivityUserDoList(param.getActivityUserList(),activityDo.getId());
        activityUserMapper.batchInsert(activityUserDOS);
        //得到完整的活动信息,放入redis
        return null;

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
        Long userNumbers = userMapper.countByIds(userIds);
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
            if(ActivityPrizeStatusEnum.forName(prize.getPrizeTiers()) == null){
                throw new ServiceException(ServiceErrorCodeConstants.ACTIVITY_PRIZE_TIER_ERROR);
            }
        });
    }
}
