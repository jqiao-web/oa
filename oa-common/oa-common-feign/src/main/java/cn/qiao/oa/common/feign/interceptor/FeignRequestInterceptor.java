package cn.qiao.oa.common.feign.interceptor;

import cn.qiao.oa.common.feign.config.FeignProperties;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Feign 请求拦截器 —— 自动透传用户上下文请求头
 * <p>
 * 当服务 A 通过 Feign 调用服务 B 时，当前请求中的用户信息 Header（如 Gateway 鉴权后写入的
 * {@code X-User-Id}、{@code X-Username} 等）不会自动传递到 Feign 请求中。
 * <p>
 * 此拦截器从 {@link RequestContextHolder} 中获取当前 HTTP 请求，
 * 将配置的请求头自动复制到 Feign 发出的请求中，确保被调用服务也能通过
 * {@code AuthInterceptor} 获取到用户上下文。
 *
 * <h3>工作原理：</h3>
 * <pre>
 * 用户请求 → Gateway（鉴权，写入 X-User-Id 等 Header）
 *          → 服务 A（AuthInterceptor 写入 ThreadLocal）
 *          → Feign 调用服务 B（本拦截器复制 Header）
 *          → 服务 B（AuthInterceptor 从 Header 恢复 ThreadLocal）
 * </pre>
 *
 * @author oa-cloud
 * @see FeignProperties#getHeaderNames()
 */
@Slf4j
@RequiredArgsConstructor
public class FeignRequestInterceptor implements RequestInterceptor {

    private final FeignProperties feignProperties;

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        // 非 Web 上下文（如定时任务触发的 Feign 调用），跳过头部透传
        if (attributes == null) {
            log.debug("非 Web 请求上下文，跳过 Feign 请求头透传");
            return;
        }

        HttpServletRequest request = attributes.getRequest();

        for (String headerName : feignProperties.getHeaderNames()) {
            String headerValue = request.getHeader(headerName);
            if (headerValue != null) {
                template.header(headerName, headerValue);
                log.trace("Feign 透传请求头: {} = {}", headerName, headerValue);
            }
        }
    }
}
