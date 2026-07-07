package com.emrys.neondraw.controller;

import com.emrys.neondraw.common.pojo.CommonResult;

import com.emrys.neondraw.controller.convert.ControllerConverter;
import com.emrys.neondraw.controller.param.DrawPrizeParam;
import com.emrys.neondraw.controller.param.ShowWinningRecordsParam;
import com.emrys.neondraw.controller.result.ShowWinningResult;
import com.emrys.neondraw.service.DrawPrizeService;
import com.emrys.neondraw.service.dto.ShowWinningRecordsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
@Slf4j
@RestController
public class DrawPrizeController {
    @Autowired
    DrawPrizeService drowPrizeService;
    @RequestMapping("/draw-prize")
    public CommonResult<Boolean> drawPrize(@RequestBody @Validated DrawPrizeParam drawPrizeParam) {
        log.info("drawPrizeParam: {}", drawPrizeParam);
        drowPrizeService.drawPrize(drawPrizeParam);
        return CommonResult.success(true);
    }

    @RequestMapping("/winning-records/show")
    public CommonResult<List<ShowWinningResult>> showWinningRecords(@Validated @RequestBody ShowWinningRecordsParam showWinningRecordsParam){
        log.info("showWinningRecordsParam: {}", showWinningRecordsParam);
        List<ShowWinningRecordsDTO> resultList = drowPrizeService.showWinningRecords(showWinningRecordsParam);
        return CommonResult.success(ControllerConverter.convertToShowWinningResultList(resultList));
    }
}
