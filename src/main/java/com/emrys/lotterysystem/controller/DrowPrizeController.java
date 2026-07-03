package com.emrys.lotterysystem.controller;

import com.emrys.lotterysystem.common.pojo.CommonResult;

import com.emrys.lotterysystem.controller.param.DrawPrizeParam;
import com.emrys.lotterysystem.service.DrawPrizeService;
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
public class DrowPrizeController {
    @Autowired
    DrawPrizeService drowPrizeService;
    @RequestMapping("/drow-prize")
    public CommonResult<Boolean> drowPrize(@RequestBody @Validated DrawPrizeParam drowPrizeParam) {
        log.info("drowPrizeParam: {}", drowPrizeParam);
        drowPrizeService.drowPrize(drowPrizeParam);
        return CommonResult.success(true);
    }
}
