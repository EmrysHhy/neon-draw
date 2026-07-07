package com.emrys.neondraw.service.impl;

import com.emrys.neondraw.controller.param.DrawPrizeParam;

import com.emrys.neondraw.controller.param.ShowWinningRecordsParam;
import com.emrys.neondraw.service.dto.ShowWinningRecordsDTO;
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
    DrawPrizeServiceImpl drawPrizeService;

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

        drawPrizeService.drawPrize(param);
    }
    @Test
    void show(){
        ShowWinningRecordsParam param = new ShowWinningRecordsParam();
        param.setActivityId(33L);
        List<ShowWinningRecordsDTO> showWinningRecordsDTOS = drawPrizeService.showWinningRecords(param);
        System.out.println(111111);
        System.out.println(showWinningRecordsDTOS.toString());
    }

}
