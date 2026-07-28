package cn.qiao.oa.common.security.config;

import cn.qiao.oa.common.security.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 * <p>
 * 注册用户认证拦截器，并配置跨域策略。
 * <ul>
 *     <li>注册 {@link AuthInterceptor} 拦截所有请求，从 Header 中提取用户信息</li>
 *     <li>配置 CORS 跨域允许前端开发服务器访问（开发环境）</li>
 * </ul>
 *
 * @author oa-cloud
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    /**
     * 注册拦截器
     * <p>
     * {@link AuthInterceptor} 拦截所有 /** 请求，
     * 从 Gateway 传递的 Header 中解析用户信息到 ThreadLocal。
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**");
    }

    /**
     * 跨域配置
     * <p>
     * 开发环境下允许前端开发服务器（localhost:5173 Vite 默认端口）跨域访问。
     * 生产环境由 Nginx 统一处理跨域，此处配置不影响。
     *
     * @param registry 跨域注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
