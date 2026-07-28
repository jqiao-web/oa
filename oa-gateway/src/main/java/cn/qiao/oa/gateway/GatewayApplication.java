package cn.qiao.oa.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * OA 网关服务启动类（端口：8080）
 * <p>
 * 作为微服务集群的统一入口，负责：
 * <ul>
 *     <li>请求路由（将前端请求转发到对应的微服务）</li>
 *     <li>全局鉴权（JWT Token 校验、白名单放行）</li>
 *     <li>用户信息透传（解析 Token 后将 userId/username 写入 Header 传递给下游）</li>
 *     <li>负载均衡（基于 Nacos 服务发现自动负载均衡）</li>
 * </ul>
 * <p>
 * 技术栈：Spring Cloud Gateway（响应式网关，基于 WebFlux）
 *
 * @author oa-cloud
 */
@SpringBootApplication(scanBasePackages = "cn.qiao.oa")
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
