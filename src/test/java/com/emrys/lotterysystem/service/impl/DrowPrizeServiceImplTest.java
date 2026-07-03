package com.emrys.lotterysystem.service.impl;

import com.emrys.lotterysystem.controller.param.DrawPrizeParam;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Emrys
 */
@SpringBootTest
class DrowPrizeServiceImplTest {

    @Autowired
    DrawPrizeServiceImpl drowPrizeService;

    @Test
    void drowPrize() {
        DrawPrizeParam.Winner winner = new DrawPrizeParam.Winner();
        winner.setUserId(101L);
        winner.setUserName("张三");

        DrawPrizeParam param = new DrawPrizeParam();
        param.setActivityId(1L);
        param.setPrizeId(2L);
        param.setWinningTime(new Date());
        param.setWinnerList(List.of(winner));

        drowPrizeService.drowPrize(param);
    }
}
