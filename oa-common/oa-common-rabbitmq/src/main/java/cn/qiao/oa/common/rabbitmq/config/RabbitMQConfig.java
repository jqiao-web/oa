package cn.qiao.oa.common.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 队列与交换机配置
 * <p>
 * 统一定义 OA 系统所需的消息队列、交换机和绑定关系。
 * 使用 Topic 交换机实现灵活的消息路由，支持延迟队列（死信队列模式）。
 *
 * <h3>队列规划：</h3>
 * <ul>
 *     <li><b>oa.notification.queue</b> - 通知队列（审批通知、任务通知、考勤通知）</li>
 *     <li><b>oa.notification.delay.queue</b> - 延迟通知队列（到期提醒等延迟消息）</li>
 *     <li><b>oa.notification.dead.queue</b> - 死信队列（处理失败的消息，供人工排查）</li>
 * </ul>
 *
 * <h3>交换机规划：</h3>
 * <ul>
 *     <li><b>oa.notification.exchange</b> - Topic 交换机，路由键匹配 notify.*</li>
 *     <li><b>oa.notification.delay.exchange</b> - Topic 交换机，延迟消息转发</li>
 * </ul>
 *
 * @author oa-cloud
 */
@Configuration
public class RabbitMQConfig {

    // ==================== 队列名称常量 ====================

    /** 通知队列 */
    public static final String NOTIFICATION_QUEUE = "oa.notification.queue";

    /** 延迟通知队列 */
    public static final String NOTIFICATION_DELAY_QUEUE = "oa.notification.delay.queue";

    /** 死信队列 */
    public static final String NOTIFICATION_DEAD_QUEUE = "oa.notification.dead.queue";

    // ==================== 交换机名称常量 ====================

    /** 通知交换机（Topic 模式） */
    public static final String NOTIFICATION_EXCHANGE = "oa.notification.exchange";

    /** 延迟通知交换机 */
    public static final String NOTIFICATION_DELAY_EXCHANGE = "oa.notification.delay.exchange";

    /** 死信交换机 */
    public static final String NOTIFICATION_DEAD_EXCHANGE = "oa.notification.dead.exchange";

    // ==================== 路由键常量 ====================

    /** 审批通知路由键 */
    public static final String ROUTING_KEY_APPROVAL = "notify.approval.*";

    /** 任务通知路由键 */
    public static final String ROUTING_KEY_TASK = "notify.task.*";

    /** 考勤通知路由键 */
    public static final String ROUTING_KEY_ATTENDANCE = "notify.attendance.*";

    /** 延迟消息路由键 */
    public static final String ROUTING_KEY_DELAY = "notify.delay.#";

    // ==================== 队列声明 ====================

    /**
     * 通知队列（持久化）
     * <p>绑定死信交换机，消费失败的消息自动转发到死信队列</p>
     */
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE)
                .withArgument("x-dead-letter-exchange", NOTIFICATION_DEAD_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "dead.notification")
                .build();
    }

    /**
     * 延迟通知队列
     * <p>
     * 消息进入该队列后，需等待 TTL 过期才会被转发到通知交换机。
     * 用于实现到期提醒等延迟通知场景。
     * </p>
     */
    @Bean
    public Queue notificationDelayQueue() {
        return QueueBuilder.durable(NOTIFICATION_DELAY_QUEUE)
                .withArgument("x-dead-letter-exchange", NOTIFICATION_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "notify.delay.expired")
                .build();
    }

    /**
     * 死信队列（持久化）
     * <p>存储消费失败的消息，供管理员排查和手动重试</p>
     */
    @Bean
    public Queue notificationDeadQueue() {
        return QueueBuilder.durable(NOTIFICATION_DEAD_QUEUE).build();
    }

    // ==================== 交换机声明 ====================

    /**
     * 通知交换机（Topic 模式）
     * <p>根据路由键将消息分发到对应的队列</p>
     */
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    /**
     * 延迟通知交换机
     */
    @Bean
    public TopicExchange notificationDelayExchange() {
        return new TopicExchange(NOTIFICATION_DELAY_EXCHANGE);
    }

    /**
     * 死信交换机
     */
    @Bean
    public TopicExchange notificationDeadExchange() {
        return new TopicExchange(NOTIFICATION_DEAD_EXCHANGE);
    }

    // ==================== 绑定关系 ====================

    /**
     * 通知队列绑定到通知交换机
     * <p>路由键匹配：notify.approval.* / notify.task.* / notify.attendance.*</p>
     */
    @Bean
    public Binding notificationBindingApproval(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(ROUTING_KEY_APPROVAL);
    }

    /** 任务通知绑定 */
    @Bean
    public Binding notificationBindingTask(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(ROUTING_KEY_TASK);
    }

    /** 考勤通知绑定 */
    @Bean
    public Binding notificationBindingAttendance(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(ROUTING_KEY_ATTENDANCE);
    }

    /** 延迟队列绑定到延迟交换机 */
    @Bean
    public Binding delayQueueBinding(Queue notificationDelayQueue, TopicExchange notificationDelayExchange) {
        return BindingBuilder.bind(notificationDelayQueue).to(notificationDelayExchange).with(ROUTING_KEY_DELAY);
    }

    /** 死信队列绑定到死信交换机 */
    @Bean
    public Binding deadQueueBinding(Queue notificationDeadQueue, TopicExchange notificationDeadExchange) {
        return BindingBuilder.bind(notificationDeadQueue).to(notificationDeadExchange).with("dead.#");
    }
}
