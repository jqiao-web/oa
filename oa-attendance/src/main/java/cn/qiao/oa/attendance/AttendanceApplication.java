package cn.qiao.oa.attendance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * OA 考勤服务启动类（端口：8083）
 * <p>
 * 负责考勤管理，包括：
 * <ul>
 *     <li>打卡管理（上班/下班打卡、外勤打卡）</li>
 *     <li>考勤规则配置（弹性工作制、固定班次、排班管理）</li>
 *     <li>考勤统计（迟到/早退/缺卡/加班统计、月度报表）</li>
 *     <li>请假/加班申请（与审批服务联动）</li>
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
@MapperScan("cn.qiao.oa.attendance.mapper")
public class AttendanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AttendanceApplication.class, args);
    }
}
