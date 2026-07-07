package com.emrys.neondraw.controller;

import com.emrys.neondraw.controller.param.DrawPrizeParam;
import com.emrys.neondraw.service.DrawPrizeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 抽奖接口测试
 *
 * @author Emrys
 */
@SpringBootTest
class DrowPrizeControllerTest {
    @Autowired
    DrawPrizeService drawPrizeService;
    @Autowired
    DrawPrizeController drowPrizeController;

    @Test
    void drowPrize() {
        DrawPrizeParam.Winner winner = new DrawPrizeParam.Winner();
        winner.setUserId(46L);
        winner.setUserName("李四");

        DrawPrizeParam param = new DrawPrizeParam();
        param.setActivityId(33L);
        param.setPrizeId(21L);
        param.setWinningTime(new Date());
        param.setWinnerList(List.of(winner));

        //CommonResult<Boolean> result = drowPrizeController.drowPrize(param);
        drawPrizeService.drawPrize(param);

    }
}
