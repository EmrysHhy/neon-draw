package com.emrys.lotterysystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emrys.lotterysystem.common.pojo.CommonResult;
import com.emrys.lotterysystem.controller.convert.ControllerConverter;
import com.emrys.lotterysystem.controller.param.CreateActivityParam;
import com.emrys.lotterysystem.controller.param.PageParam;
import com.emrys.lotterysystem.controller.result.ActivityDetailResult;
import com.emrys.lotterysystem.controller.result.FindActivityListResult;
import com.emrys.lotterysystem.controller.result.createActivityResult;
import com.emrys.lotterysystem.service.ActivityService;
import com.emrys.lotterysystem.service.dto.ActivityDTO;
import com.emrys.lotterysystem.service.dto.ActivityDetailDTO;
import com.emrys.lotterysystem.service.dto.CreateActivityDTO;
import com.emrys.lotterysystem.service.dto.PageListDTO;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Emrys
 * content:
 */
@Slf4j
@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    ActivityService activityService;

    /**
     * 创建活动
     *
     * @param param
     * @return
     */
    @RequestMapping("/create")
    public CommonResult<createActivityResult> createActivity(@RequestBody @Validated CreateActivityParam param) {
        log.info("create activity, param: {}", param);
        CreateActivityDTO activityDTO = activityService.createActivity(param);
        return CommonResult.success(
                ControllerConverter.convertToCreateActivityResult(activityDTO));
    }

    /**
     *
     */
    @RequestMapping("/find-list")
    public CommonResult<FindActivityListResult> findActivityListResult(PageParam param) {
        log.info("find activity list, param: {}", param);
        PageListDTO<ActivityDTO> pageListDTO = activityService.findActivityList(param);
        return CommonResult.success(
                ControllerConverter.convertToFindActivityListResult(pageListDTO));
    }
    /**
     * 查询活动详情（含奖品、参与人员）
     *
     * @param activityId 活动id
     * @return 活动详情
     */
    @RequestMapping("/detail/find")
    public CommonResult<ActivityDetailResult> findActivityDetail(@RequestParam Long activityId) {
        log.info("find activity detail, activityId: {}", activityId);
        ActivityDetailDTO activityDetailDTO = activityService.findActivityDetail(activityId);
        return CommonResult.success(
                ControllerConverter.convertToActivityDetailResult(activityDetailDTO));
    }
    
}
