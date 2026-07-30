package cn.qiao.oa.common.feign.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

/**
 * Feign 自定义配置属性
 * <p>
 * 通过 {@code application.yml} 中 {@code oa.feign} 前缀进行配置。
 * <ul>
 *     <li>{@code header-names} - Feign 调用时需要透传的请求头名称列表</li>
 *     <li>{@code connect-timeout} - 连接超时时间（毫秒）</li>
 *     <li>{@code read-timeout} - 读取超时时间（毫秒）</li>
 *     <li>{@code logger-level} - Feign 日志级别</li>
 * </ul>
 *
 * <h3>配置示例：</h3>
 * <pre>{@code
 * oa:
 *   feign:
 *     connect-timeout: 5000
 *     read-timeout: 10000
 *     logger-level: full
 *     header-names:
 *       - X-User-Id
 *       - X-Username
 *       - X-Dept-Id
 *       - X-Data-Scope
 * }</pre>
 *
 * @author oa-cloud
 */
@Data
@ConfigurationProperties(prefix = "oa.feign")
public class FeignProperties {

    /**
     * Feign 调用时需要从当前请求透传到目标服务的请求头名称列表
     * <p>
     * 默认透传 Gateway 鉴权后写入的用户上下文 Header。
     */
    private List<String> headerNames = Arrays.asList(
            "X-User-Id",
            "X-Username",
            "X-Dept-Id",
            "X-Data-Scope",
            "X-Roles",
            "Authorization"
    );

    /** 连接超时时间（毫秒），默认 5 秒 */
    private Integer connectTimeout = 5000;

    /** 读取超时时间（毫秒），默认 10 秒 */
    private Integer readTimeout = 10000;

    /**
     * Feign 日志级别
     * <p>
     * 可选值：none / basic / headers / full，默认 basic。
     */
    private String loggerLevel = "basic";
}
