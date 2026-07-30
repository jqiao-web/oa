package cn.qiao.oa.common.swagger.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger / OpenAPI 3 自动配置
 * <p>
 * 提供默认的 OpenAPI 文档信息，各服务可通过自定义 {@link OpenAPI} Bean 覆盖。
 */
@Configuration
public class SwaggerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(OpenAPI.class)
    public OpenAPI defaultOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OA Cloud 协同办公系统")
                        .description("Spring Cloud 微服务 OA 协同办公系统 API 文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("OA Cloud")
                                .email("admin@oa-cloud.cn")))
                // 相对路径 Server：通过 Gateway 访问时请求走 Gateway，直连服务时走服务自身
                .addServersItem(new Server().url("/").description("当前访问地址"))
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .components(new Components()
                        .addSecuritySchemes("Authorization",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("请输入 JWT Token（无需添加 Bearer 前缀）")));
    }

    /**
     * 强制使用相对路径 Server，覆盖 SpringDoc 自动检测的服务地址。
     * 这样通过 Gateway 访问 Swagger UI 时，请求走 Gateway；直连服务时走服务自身。
     */
    @Bean
    public GlobalOpenApiCustomizer forceRelativeServer() {
        return openApi -> openApi.setServers(
                List.of(new Server().url("/").description("当前访问地址")));
    }
}
