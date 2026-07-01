package com.emrys.lotterysystem.controller;

import com.emrys.lotterysystem.common.pojo.CommonResult;
import com.emrys.lotterysystem.controller.convert.ControllerConverter;
import com.emrys.lotterysystem.controller.param.CreateActivityParam;
import com.emrys.lotterysystem.controller.param.PageParam;
import com.emrys.lotterysystem.controller.result.FindActivityListResult;
import com.emrys.lotterysystem.controller.result.createActivityResult;
import com.emrys.lotterysystem.service.ActivityService;
import com.emrys.lotterysystem.service.dto.ActivityDTO;
import com.emrys.lotterysystem.service.dto.CreateActivityDTO;
import com.emrys.lotterysystem.service.dto.PageListDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
