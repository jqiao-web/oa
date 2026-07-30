package cn.qiao.oa.common.feign.config;

import cn.qiao.oa.common.feign.decoder.FeignErrorDecoder;
import cn.qiao.oa.common.feign.interceptor.FeignRequestInterceptor;
import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Feign 自动配置
 * <p>
 * 自动装配以下组件：
 * <ul>
 *     <li>{@link FeignRequestInterceptor} - 透传用户上下文请求头</li>
 *     <li>{@link FeignErrorDecoder} - 统一错误解码</li>
 *     <li>{@link Request.Options} - 超时配置</li>
 *     <li>{@link Logger.Level} - 日志级别</li>
 *     <li>{@link Retryer} - 重试策略</li>
 * </ul>
 *
 * <h3>各服务使用方式：</h3>
 * <pre>{@code
 * // 1. 引入 oa-common-feign 依赖
 * // 2. 启动类添加 @EnableFeignClients(basePackages = "cn.qiao.oa.xxx.feign")
 * // 3. 定义 Feign Client 接口
 * @FeignClient(name = "oa-auth", fallbackFactory = AuthClientFallbackFactory.class)
 * public interface AuthFeignClient {
 *     @GetMapping("/api/auth/users/{id}")
 *     R<UserVO> getUserById(@PathVariable("id") Long id);
 * }
 * }</pre>
 *
 * @author oa-cloud
 */
@Configuration
@EnableConfigurationProperties(FeignProperties.class)
public class FeignAutoConfiguration {

    /**
     * Feign 请求拦截器 —— 自动透传用户上下文 Header
     */
    @Bean
    @ConditionalOnMissingBean(RequestInterceptor.class)
    public RequestInterceptor feignRequestInterceptor(FeignProperties properties) {
        return new FeignRequestInterceptor(properties);
    }

    /**
     * Feign 错误解码器 —— 将 HTTP 错误响应转换为 BusinessException
     */
    @Bean
    @ConditionalOnMissingBean(ErrorDecoder.class)
    public ErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder();
    }

    /**
     * Feign 超时配置
     * <p>
     * 通过 {@code oa.feign.connect-timeout} 和 {@code oa.feign.read-timeout} 配置。
     */
    @Bean
    public Request.Options feignRequestOptions(FeignProperties properties) {
        return new Request.Options(
                properties.getConnectTimeout(), TimeUnit.MILLISECONDS,
                properties.getReadTimeout(), TimeUnit.MILLISECONDS,
                true  // followRedirects
        );
    }

    /**
     * Feign 日志级别
     * <p>
     * 通过 {@code oa.feign.logger-level} 配置，可选值：none / basic / headers / full。
     */
    @Bean
    public Logger.Level feignLoggerLevel(FeignProperties properties) {
        return switch (properties.getLoggerLevel().toLowerCase()) {
            case "none" -> Logger.Level.NONE;
            case "headers" -> Logger.Level.HEADERS;
            case "full" -> Logger.Level.FULL;
            default -> Logger.Level.BASIC;
        };
    }

    /**
     * Feign 重试策略
     * <p>
     * 默认：初始间隔 100ms，最大间隔 1s，最多重试 3 次。
     */
    @Bean
    @ConditionalOnMissingBean(Retryer.class)
    public Retryer feignRetryer() {
        return new Retryer.Default(100, 1000, 3);
    }
}
