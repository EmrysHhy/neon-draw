package com.emrys.lotterysystem.service.mq;



import com.emrys.lotterysystem.common.utils.EmailUtil;

import com.emrys.lotterysystem.controller.convert.ControllerConverter;

import com.emrys.lotterysystem.controller.param.DrawPrizeParam;

import com.emrys.lotterysystem.controller.result.SendWinnerEmailResult;

import com.emrys.lotterysystem.dao.dataobject.WinningRecordDO;

import com.emrys.lotterysystem.service.DrawPrizeService;

import com.emrys.lotterysystem.service.constants.MQConstant;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;



import java.util.List;
import java.util.Map;



import static com.emrys.lotterysystem.common.config.DirectRabbitConfig.QUEUE_NAME;



/**
 * 抽奖 MQ 消费者
 * @author Emrys
 */

@Slf4j

@Component

@RabbitListener(queues = QUEUE_NAME)

public class MQReceiver {



    @Autowired

    DrawPrizeService drawPrizeService;

    @Autowired

    ObjectMapper objectMapper;

    @Autowired
    @Qualifier("asyncServiceExecutor")
    private ThreadPoolTaskExecutor threadPoolExecutor;

    @Autowired

    EmailUtil emailUtil;



    @RabbitHandler
    public void process(Map<String, Object> message) {
        log.info("MQ消费者收到消息: {}", message);
        Object messageData = message.get(MQConstant.MESSAGE_DATA);
        DrawPrizeParam param = objectMapper.convertValue(messageData, DrawPrizeParam.class);

        try {

            // 1. DB 事务：校验 → 改状态 → 存中奖记录（失败自动回滚）

            List<WinningRecordDO> winningRecordList = drawPrizeService.processDrawPrize(param);

            // 2. 事务外：刷新 Redis（中途失败回滚已写缓存，不影响 DB）
            drawPrizeService.refreshCacheAfterDraw(param, winningRecordList);
            // 3. 事务外：异步发邮件（缓存失败仍继续发信）

            synExecute(winningRecordList);

        } catch (Exception e) {

            log.error("MQ消费者处理消息失败: {}", e.getMessage(), e);

            throw e;

        }

    }



    private void synExecute(List<WinningRecordDO> winningRecordList) {

        threadPoolExecutor.execute(() -> sendEmailToUser(winningRecordList));

    }



    private void sendEmailToUser(List<WinningRecordDO> winningRecordList) {

        for (WinningRecordDO record : winningRecordList) {

            SendWinnerEmailResult sendWinnerEmailResult = ControllerConverter.convertToSendWinnerEmailResult(record);

            try {

                emailUtil.sendWinningNotification(sendWinnerEmailResult);

            } catch (Exception e) {

                log.error("发送中奖邮件失败, winnerId: {}, email: {}",

                        record.getWinnerId(), record.getWinnerEmail(), e);

            }

        }

    }

}


