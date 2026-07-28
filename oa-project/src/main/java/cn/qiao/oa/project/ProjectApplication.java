package cn.qiao.oa.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * OA 项目服务启动类（端口：8084）
 * <p>
 * 负责项目与任务管理，包括：
 * <ul>
 *     <li>项目管理（创建/编辑/归档项目、项目成员管理）</li>
 *     <li>任务管理（创建/分配/完成任务、任务优先级、看板视图）</li>
 *     <li>任务看板（待办/进行中/已完成状态流转）</li>
 *     <li>项目统计（任务完成率、工时统计、甘特图）</li>
 * </ul>
 * <p>
 * 依赖模块：oa-common-core、oa-common-redis、oa-common-security、
 * oa-common-mybatis、oa-common-rabbitmq
 *
 * @author oa-cloud
 */
@SpringBootApplication(scanBasePackages = "cn.qiao.oa")
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("cn.qiao.oa.project.mapper")
public class ProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }
}
