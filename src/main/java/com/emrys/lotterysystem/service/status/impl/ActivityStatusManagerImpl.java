package com.emrys.lotterysystem.service.status.impl;

import com.emrys.lotterysystem.common.errorcode.ServiceErrorCodeConstants;
import com.emrys.lotterysystem.common.exception.ServiceException;
import com.emrys.lotterysystem.service.ActivityService;
import com.emrys.lotterysystem.service.dto.ConvertActivityStatusDTO;
import com.emrys.lotterysystem.service.status.ActivityStatusManager;
import com.emrys.lotterysystem.service.status.StatusOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Emrys
 * content:状态转换处理类
 */
@Slf4j
@Service
public class ActivityStatusManagerImpl implements ActivityStatusManager {
    @Autowired
    ActivityService activityService;
    @Autowired
    private final Map<String, StatusOperator> operatorMap = new HashMap<>();

    /**
     * 状态转换器实现,保证事务一致
     * @param convertActivityStatusDTO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void handleEvent(ConvertActivityStatusDTO convertActivityStatusDTO) {
        //状态依赖性,链式设计
        //扩展性增强
        if (operatorMap.isEmpty()){
            log.warn("ActivityStatusManagerImpl: operatorMap is empty, please check the configuration");
        }
        Map<String, StatusOperator> map = new HashMap<>(operatorMap); //这个map可以增删,用于处理以下逻辑

        //1.先处理人员和奖品状态转换
        Boolean update =  processConvertStatus(convertActivityStatusDTO,map,1);
        if(update){
            //2.后处理活动();
            processConvertStatus(convertActivityStatusDTO, map, 2);
        }else{
            log.error("活动奖品表或活动人员表转化失败");
            throw new ServiceException(ServiceErrorCodeConstants.STATUS_CONVERT_FAIL);
        }
    }

    /**
     * 核心逻辑
     * @param convertActivityStatusDTO
     * @param map
     * @param sequence
     * @return
     */
    private Boolean processConvertStatus(ConvertActivityStatusDTO convertActivityStatusDTO,
                                         Map<String, StatusOperator> map,
                                         int sequence) {
        //1.遍历map查找当前map里面需要转换的
        //2.如果找到了,就执行转换,并且从map里面删除这个key
        Iterator<Entry<String,StatusOperator>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Entry<String,StatusOperator> entry = iterator.next();
            String key = entry.getKey();
            StatusOperator operator = entry.getValue();
            if (sequence == operator.sequence()) {
                if(operator.convert(convertActivityStatusDTO)){ //转换成功
                    iterator.remove();
                }else{ //转换失败
                    log.error("ActivityStatusManagerImpl: convert status failed, key: {}, convertActivityStatusDTO: {}",
                            key, convertActivityStatusDTO);
                    throw new ServiceException(ServiceErrorCodeConstants.STATUS_CONVERT_FAIL);
                }

            }
        }
        return true;
    }
}
