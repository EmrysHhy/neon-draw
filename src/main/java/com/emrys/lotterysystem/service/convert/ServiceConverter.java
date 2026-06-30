package com.emrys.lotterysystem.service.convert;

import ch.qos.logback.classic.Logger;
import com.emrys.lotterysystem.controller.param.ActivityPrizeParam;
import com.emrys.lotterysystem.controller.param.ActivityUserParam;
import com.emrys.lotterysystem.dao.dataobject.*;
import com.emrys.lotterysystem.service.dto.ActivityDetailDTO;
import com.emrys.lotterysystem.service.dto.PrizeDTO;
import com.emrys.lotterysystem.service.dto.UserDTO;
import com.emrys.lotterysystem.service.enums.ActivityPrizeStatusEnum;
import com.emrys.lotterysystem.service.enums.ActivityStatusEnum;
import com.emrys.lotterysystem.service.enums.ActivityUserStatusEnum;
import com.emrys.lotterysystem.service.enums.PrizeTierEnum;
import com.emrys.lotterysystem.service.enums.UserIdentityEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
@Slf4j
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
            activityPrizeDO.setPrizeTiers(param.getPrizeTiers());
            activityPrizeDO.setStatus(ActivityPrizeStatusEnum.NOT_DRAW.name());
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

    /**
     * 将 ActivityDo、ActivityPrizeDO 列表、PrizeDO 列表、ActivityUserDO 列表转换为 ActivityDetailDTO
     * @param activityDo
     * @param activityPrizeDOS
     * @param prizeDOList
     * @param activityUserDOS
     * @return
     */
    public static ActivityDetailDTO convertToActivityDetailDTO(
            ActivityDo activityDo,
            List<ActivityPrizeDO> activityPrizeDOS,
            List<PrizeDO> prizeDOList,
            List<ActivityUserDO> activityUserDOS) {

        ActivityDetailDTO activityDetailDTO = new ActivityDetailDTO();
        activityDetailDTO.setActivityId(activityDo.getId());
        activityDetailDTO.setActivityName(activityDo.getActivityName());
        activityDetailDTO.setDesc(activityDo.getDescription());
        activityDetailDTO.setStatus(ActivityStatusEnum.forName(activityDo.getStatus()));

        // 将 PrizeDO 建成 Map，方便按 id 查找
        Map<Long, PrizeDO> prizeDOMap = prizeDOList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(PrizeDO::getId, Function.identity()));

        // 奖品列表：合并 ActivityPrizeDO + PrizeDO
        List<ActivityDetailDTO.PrizeDetail> prizeDetails = activityPrizeDOS.stream()
                .filter(Objects::nonNull)
                .map(ap -> {
                    PrizeDO prizeDO = prizeDOMap.get(ap.getPrizeId());
                    if (prizeDO == null) {
                        return null;
                    }
                    ActivityDetailDTO.PrizeDetail detail = new ActivityDetailDTO.PrizeDetail();
                    detail.setPrizeId(prizeDO.getId());
                    detail.setPrizeName(prizeDO.getName());
                    detail.setImageUrl(prizeDO.getImageUrl());
                    detail.setPrice(prizeDO.getPrice());
                    detail.setDescription(prizeDO.getDescription());
                    detail.setPrizeAmount(ap.getPrizeAmount().longValue());
                    detail.setTiers(PrizeTierEnum.forName(ap.getPrizeTiers()));
                    detail.setStatus(ActivityPrizeStatusEnum.forName(ap.getStatus()));
                    return detail;
                }).toList();
        activityDetailDTO.setPrizeList(prizeDetails);

        // 人员列表：直接从 ActivityUserDO 映射
        List<ActivityDetailDTO.UserDetail> userDetails = activityUserDOS.stream()
                .filter(Objects::nonNull)
                .map(au -> {
                    ActivityDetailDTO.UserDetail detail = new ActivityDetailDTO.UserDetail();
                    detail.setUserId(au.getUserId());
                    detail.setUserName(au.getUserName());
                    detail.setStatus(ActivityUserStatusEnum.forName(au.getStatus()));
                    return detail;
                }).toList();
        activityDetailDTO.setUserList(userDetails);

        return activityDetailDTO;
    }
}
