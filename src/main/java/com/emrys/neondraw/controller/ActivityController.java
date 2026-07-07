package com.emrys.neondraw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emrys.neondraw.common.pojo.CommonResult;
import com.emrys.neondraw.controller.convert.ControllerConverter;
import com.emrys.neondraw.controller.param.CreateActivityParam;
import com.emrys.neondraw.controller.param.PageParam;
import com.emrys.neondraw.controller.result.ActivityDetailResult;
import com.emrys.neondraw.controller.result.FindActivityListResult;
import com.emrys.neondraw.controller.result.createActivityResult;
import com.emrys.neondraw.service.ActivityService;
import com.emrys.neondraw.service.dto.ActivityDTO;
import com.emrys.neondraw.service.dto.ActivityDetailDTO;
import com.emrys.neondraw.service.dto.CreateActivityDTO;
import com.emrys.neondraw.service.dto.PageListDTO;

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
