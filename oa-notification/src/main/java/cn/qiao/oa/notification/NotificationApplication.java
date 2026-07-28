package cn.qiao.oa.notification;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * OA 通知服务启动类（端口：8086）
 * <p>
 * 负责系统通知管理，包括：
 * <ul>
 *     <li>消息队列消费（监听 RabbitMQ 审批/任务/考勤通知队列）</li>
 *     <li>通知管理（站内信创建/查询/已读/删除）</li>
 *     <li>WebSocket 实时推送（在线用户实时通知）</li>
 *     <li>通知统计（未读数量、通知类型分布）</li>
 * </ul>
 * <p>
 * 依赖模块：oa-common-core、oa-common-redis、oa-common-security、
 * oa-common-mybatis、oa-common-rabbitmq
 *
 * @author oa-cloud
 */
@SpringBootApplication(scanBasePackages = "cn.qiao.oa")
@EnableDiscoveryClient
@MapperScan("cn.qiao.oa.notification.mapper")
public class NotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }
}
