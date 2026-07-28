package cn.qiao.oa.approval;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * OA 审批服务启动类（端口：8082）
 * <p>
 * 负责审批流程管理，包括：
 * <ul>
 *     <li>审批模板管理（创建/编辑/启停审批流程模板）</li>
 *     <li>审批实例管理（提交/审批/撤回/催办审批单）</li>
 *     <li>审批节点流转（多级审批、会签、或签、条件分支）</li>
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
@MapperScan("cn.qiao.oa.approval.mapper")
public class ApprovalApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApprovalApplication.class, args);
    }
}
