package com.emrys.lotterysystem.controller.convert;

import com.emrys.lotterysystem.common.errorcode.ControllerErrorCodeConstants;
import com.emrys.lotterysystem.common.exception.ControllerException;
import com.emrys.lotterysystem.controller.result.*;
import com.emrys.lotterysystem.service.dto.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
public class ControllerConverter {
    /**
     * 将 UserDTO 列表转换为 BaseUserInfoResult 列表
     */
    public static List<BaseUserInfoResult> convertToBaseUserInfoResultList(List<UserDTO> userDTOS) {
        if(userDTOS == null){
            return List.of();
        }
        return userDTOS.stream().filter(Objects::nonNull).map(userDTO -> {
            BaseUserInfoResult baseUserInfoResult = new BaseUserInfoResult();
            baseUserInfoResult.setUserId(userDTO.getUserId());
            baseUserInfoResult.setUserName(userDTO.getUserName());
            baseUserInfoResult.setIdentity(userDTO.getIdentity() != null ? userDTO.getIdentity().name() : "NORMAL");
            return baseUserInfoResult;
        }).toList();
    }

    /**
     * 将 UserLoginDTO 转换为 UserLoginResult
     */
    public static UserLoginResult convertToUserLoginResult(UserLoginDTO userLoginDTO) {
        UserLoginResult userLoginResult = new UserLoginResult();
        if(userLoginDTO == null){
            log.error("user login failed, userLoginDTO is null");
            throw new ControllerException(ControllerErrorCodeConstants.LOGIN_ERROR);
        }
        userLoginResult.setToken(userLoginDTO.getToken());
        userLoginResult.setIdentity(userLoginDTO.getMandatoryIdentity().name());
        return userLoginResult;
    }
    /**
     * 将 UserRegisterDTO 转换为 UserRegisterResult
     */
    public static UserRegisterResult convertToUserRegisterResult(UserRegisterDTO userRegisterDTO) {
        UserRegisterResult userRegisterResult = new UserRegisterResult();
        if(userRegisterDTO == null){
            log.error("user register failed, userRegisterDTO is null");
            throw new ControllerException(ControllerErrorCodeConstants.REGISTER_ERROR);
        }
        userRegisterResult.setUserId(userRegisterDTO.getUserId());
        return userRegisterResult;
    }

    /**
     * 将 PageListDTO<PrizeDTO> 转换为 FindPrizeListResult
     */
    public static FindPrizeListResult convertToFindPrizeListResult(PageListDTO<PrizeDTO> pageListDTO) {
        FindPrizeListResult findPrizeListResult = new FindPrizeListResult();
        if(pageListDTO == null) {
            log.error("find prize list failed, pageListDTO is null");
            throw new ControllerException(ControllerErrorCodeConstants.FIND_PRIZE_LIST_ERROR);
        }

        findPrizeListResult.setTotal(pageListDTO.getTotal());
        List<PrizeDTO> prizeDTOS = pageListDTO.getRecords();
        if (prizeDTOS != null) {
            findPrizeListResult.setRecords(prizeDTOS.stream().filter(Objects::nonNull).map(prizeDTO -> {
                FindPrizeListResult.PrizeInfo prizeInfo = new FindPrizeListResult.PrizeInfo();
                prizeInfo.setPrizeId(prizeDTO.getPrizeId());
                prizeInfo.setPrizeName(prizeDTO.getPrizeName());
                prizeInfo.setDescription(prizeDTO.getDescription());
                prizeInfo.setPrice(prizeDTO.getPrice());
                prizeInfo.setImageUrl(prizeDTO.getImageUrl());
                return prizeInfo;
            }).toList());
        }
        return findPrizeListResult;
    }

    /**
     * 将 CreateActivityDTO 转换为 CreateActivityResult

     */
    public static createActivityResult convertToCreateActivityResult(CreateActivityDTO activityDTO) {
        if(activityDTO == null) {
            log.error("create activity failed, activityDTO is null");
            throw new ControllerException(ControllerErrorCodeConstants.CREATE_ACTIVITY_ERROR);
        }
        createActivityResult result = new createActivityResult();
        result.setActivityId(activityDTO.getActivityId());
        return result;
    }
}
