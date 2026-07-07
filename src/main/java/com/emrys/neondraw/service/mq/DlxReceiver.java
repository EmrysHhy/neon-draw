package com.emrys.neondraw.service.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.emrys.neondraw.common.config.DirectRabbitConfig.*;

/**
 *
 * @author Emrys
 * content:
 */
@Component
@RabbitListener(queues = DLX_QUEUE_NAME)
public class DlxReceiver {
    @Autowired
    RabbitTemplate rabbitTemplate;
//    @RabbitHandler
//    public void process(Map<String,String> message){
//        //死信队列的消息应当先处理异常然后重新发给正常的RabbitMQ队列中,进行重新消费
//        rabbitTemplate.convertAndSend(EXCHANGE_NAME,ROUTING,message);
//
//
//    }

}
