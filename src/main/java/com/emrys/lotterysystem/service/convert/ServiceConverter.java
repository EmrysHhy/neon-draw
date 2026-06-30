package com.emrys.lotterysystem.service.convert;

import com.emrys.lotterysystem.controller.param.ActivityPrizeParam;
import com.emrys.lotterysystem.controller.param.ActivityUserParam;
import com.emrys.lotterysystem.dao.dataobject.ActivityPrizeDO;
import com.emrys.lotterysystem.dao.dataobject.ActivityUserDO;
import com.emrys.lotterysystem.dao.dataobject.PrizeDO;
import com.emrys.lotterysystem.dao.dataobject.UserDO;
import com.emrys.lotterysystem.service.dto.PrizeDTO;
import com.emrys.lotterysystem.service.dto.UserDTO;
import com.emrys.lotterysystem.service.enums.ActivityUserStatusEnum;
import com.emrys.lotterysystem.service.enums.UserIdentityEnum;

import java.util.List;
import java.util.Objects;

public class ServiceConverter {
    /**
     * 将 UserDO 列表转换为 UserDTO 列表
     */
    public static List<UserDTO> convertToUserDTOList(List<UserDO> userDOS){
        if(userDOS == null){
            return List.of();
        }
        return userDOS.stream().filter(Objects::nonNull).map(userDO -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userDO.getId());
            userDTO.setUserName(userDO.getUserName());
            userDTO.setEmail(userDO.getEmail());
            userDTO.setIdentity(UserIdentityEnum.forName(userDO.getIdentity()));
            userDTO.setPhoneNumber(userDO.getPhoneNumber().getValue());
            return userDTO;
        }).toList();
    }

    /**
     * 将 PrizeDO 列表转换为 PrizeDTO 列表
     */
    public static List<PrizeDTO> convertToPrizeDTOList(List<PrizeDO> prizeDOList) {
        if(prizeDOList == null){
            return List.of();
        }
        return prizeDOList.stream().filter(Objects::nonNull).map(prizeDO -> {
            PrizeDTO prizeDTO = new PrizeDTO();
            prizeDTO.setPrizeId(prizeDO.getId());
            prizeDTO.setPrizeName(prizeDO.getName());
            prizeDTO.setDescription(prizeDO.getDescription());
            prizeDTO.setPrice(prizeDO.getPrice());
            prizeDTO.setImageUrl(prizeDO.getImageUrl());
            return prizeDTO;
        }).toList();
    }


    /**
     * 将 ActivityPrizeParam 列表转换为 ActivityPrizeDO 列表
     */
    public static List<ActivityPrizeDO> convertToActivityPrizeDoList(List<ActivityPrizeParam> activityPrizeList,Long activityId) {
        if (activityPrizeList == null) {
            return List.of();
        }
        return activityPrizeList.stream().filter(Objects::nonNull).map(param -> {
            ActivityPrizeDO activityPrizeDO = new ActivityPrizeDO();
            activityPrizeDO.setActivityId(activityId);
            activityPrizeDO.setPrizeId(param.getPrizeId());
            activityPrizeDO.setPrizeAmount(param.getPrizeAmount() != null ? param.getPrizeAmount().intValue() : null);
            activityPrizeDO.setPrizeTiers(Integer.valueOf(param.getPrizeTiers()));
            return activityPrizeDO;
        }).toList();
    }

    /**
     * 将 ActivityUserParam 列表转换为 ActivityUserDO 列表
     */
    public static List<ActivityUserDO> convertToActivityUserDoList(List<ActivityUserParam> activityUserList,Long activityId) {
        if (activityUserList == null) {
            return List.of();
        }
        return activityUserList.stream().filter(Objects::nonNull).map(param -> {
            ActivityUserDO activityUserDO = new ActivityUserDO();
            activityUserDO.setActivityId(activityId);
            activityUserDO.setUserId(param.getUserId());
            activityUserDO.setUserName(param.getUserName());
            activityUserDO.setStatus(ActivityUserStatusEnum.NOT_WIN.name());
            return activityUserDO;
        }).toList();
    }
}
