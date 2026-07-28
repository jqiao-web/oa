package cn.qiao.oa.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * OA 用户权限服务启动类（端口：8081）
 * <p>
 * 负责用户与权限管理，包括：
 * <ul>
 *     <li>用户管理（注册/登录/个人信息/密码重置）</li>
 *     <li>部门管理（组织架构树、部门 CRUD）</li>
 *     <li>角色管理（角色 CRUD、角色分配、数据权限配置）</li>
 *     <li>菜单管理（菜单树 CRUD、权限标识绑定）</li>
 * </ul>
 * <p>
 * 依赖模块：oa-common-core、oa-common-redis、oa-common-security、oa-common-mybatis
 *
 * @author oa-cloud
 */
@SpringBootApplication(scanBasePackages = "cn.qiao.oa")
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("cn.qiao.oa.auth.mapper")
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
