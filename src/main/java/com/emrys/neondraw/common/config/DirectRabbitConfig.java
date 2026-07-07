package com.emrys.neondraw.common.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

/**
 * RabbitMQ Direct 模式配置（含死信队列）
 * <p>
 * 消息流转：生产者 → DirectExchange →（routingKey 精确匹配）→ DirectQueue → 消费者
 * 死信流转：DirectQueue 中无法正常消费的消息 → DlxDirectExchange → DlxDirectQueue
 *
 * @author yibo
 */
@Configuration
public class DirectRabbitConfig {

    /** 普通业务队列名称，消费者 {@code @RabbitListener(queues = QUEUE_NAME)} 监听此队列 */
    public static final String QUEUE_NAME = "DirectQueue";
    /** 普通 Direct 交换机名称，生产者发送消息时需指定此 exchange */
    public static final String EXCHANGE_NAME = "DirectExchange";
    /** 普通路由键：Direct 交换机按 routingKey 做精确匹配，匹配成功才投递到队列 */
    public static final String ROUTING = "DirectRouting";

    /** 死信队列名称，存放过期、被拒绝、队列满等无法正常消费的消息，便于人工排查或补偿 */
    public static final String DLX_QUEUE_NAME = "DlxDirectQueue";
    /** 死信交换机名称，普通队列中产生的死信会先转发到此交换机，再路由到死信队列 */
    public static final String DLX_EXCHANGE_NAME = "DlxDirectExchange";
    /** 死信路由键：死信交换机转发到死信队列时使用的匹配键（Direct 模式需完全一致） */
    public static final String DLX_ROUTING = "DlxDirectRouting";

    /**
     * 声明普通业务队列，并配置死信转发规则
     * <p>
     * 应用启动时向 RabbitMQ 注册队列；若已存在且参数一致则复用，参数不一致会报错
     */
    @Bean
    public Queue directQueue() {
        return QueueBuilder
                // durable(QUEUE_NAME)：队列名称 + 持久化
                // - 名称：在 Broker 中的唯一标识，消费者监听时使用
                // - durable=true：队列元数据写入磁盘，RabbitMQ 重启后队列仍存在（队列内未持久化的消息仍可能丢失）
                .durable(QUEUE_NAME)
                // deadLetterExchange：死信目标交换机
                // - 当消息变为死信（被拒绝且 requeue=false、TTL 过期、队列达到 max-length 等）时，Broker 会把消息重新发布到该交换机
                .deadLetterExchange(DLX_EXCHANGE_NAME)
                // deadLetterRoutingKey：消息进入死信交换机时携带的路由键
                // - 需与死信队列绑定时的 routingKey（DLX_ROUTING）一致，才能正确路由到 DlxDirectQueue
                .deadLetterRoutingKey(DLX_ROUTING)
                .build();
    }

    /**
     * 声明普通 Direct 交换机
     * <p>
     * Direct 类型：根据 routingKey 与绑定键完全相等来路由，适合点对点、按业务类型精确分发
     */
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(
                EXCHANGE_NAME, // name：交换机名称，生产者 send 时需指定
                true,          // durable：持久化，Broker 重启后交换机定义仍存在
                false          // autoDelete：false 表示即使没有队列绑定，交换机也不会被自动删除
        );
    }

    /**
     * 绑定普通队列与交换机，建立「交换机 → 队列」的路由关系
     * <p>
     * 仅当消息的 routingKey 等于 ROUTING 时，才会从 DirectExchange 进入 DirectQueue
     */
    @Bean
    Binding bindingDirect() {
        return BindingBuilder
                .bind(directQueue())   // queue：绑定目标队列
                .to(directExchange())  // exchange：消息来源交换机
                .with(ROUTING);        // routingKey：绑定键，Direct 模式下必须与消息 routingKey 完全一致
    }

    /**
     * 声明死信队列（存放异常消息的兜底队列）
     */
    @Bean
    public Queue dlxQueue() {
        return new Queue(
                DLX_QUEUE_NAME, // name：死信队列名称
                true            // durable：持久化，重启后死信队列仍存在，避免异常消息随 Broker 重启丢失元数据
                // 未使用的可选参数说明：
                // exclusive=false（默认）：队列不仅限创建连接的 Channel 使用，多消费者可共享
                // autoDelete=false（默认）：无消费者时队列不会自动删除
        );
    }

    /**
     * 声明死信 Direct 交换机
     * <p>
     * 接收来自普通队列转发的死信，再按 DLX_ROUTING 路由到死信队列
     */
    @Bean
    DirectExchange dlxExchange() {
        return new DirectExchange(
                DLX_EXCHANGE_NAME, // name：死信交换机名称，需与 directQueue 的 deadLetterExchange 一致
                true,              // durable：持久化
                false              // autoDelete：不自动删除
        );
    }

    /**
     * 绑定死信队列与死信交换机
     * <p>
     * 普通队列产生的死信经 DlxDirectExchange 且 routingKey=DLX_ROUTING 时，进入 DlxDirectQueue
     */
    @Bean
    Binding bindingDlx() {
        return BindingBuilder
                .bind(dlxQueue())    // queue：死信存放队列
                .to(dlxExchange())   // exchange：死信交换机
                .with(DLX_ROUTING);  // routingKey：须与 directQueue 的 deadLetterRoutingKey 一致
    }

    /**
     * 消息序列化/反序列化转换器
     * <p>
     * - 发送：Java 对象 → JSON 字节写入消息体（contentType 一般为 application/json）
     * - 接收：消息体 JSON → 反序列化为 {@code @RabbitListener} 方法参数类型
     * <p>
     * 未配置时默认使用 SimpleMessageConverter（仅适合 String/byte[]），配置后可直传 DTO
     */
    @Bean
    public MessageConverter jsonMessageConverter(JsonMapper jsonMapper) {
        return new JacksonJsonMessageConverter(jsonMapper);
    }
}
