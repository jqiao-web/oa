package cn.qiao.oa.gateway.filter;

import cn.qiao.oa.common.core.constant.CommonConstant;
import cn.qiao.oa.common.core.enums.ResponseStatusEnum;
import cn.qiao.oa.common.jwt.utils.JwtUtils;
import cn.qiao.oa.common.redis.cache.CacheService;
import cn.qiao.oa.gateway.config.GatewayWhitelistProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 网关全局鉴权过滤器
 * <p>
 * 使用 {@link JwtUtils}（来自 oa-common-jwt 模块）解析和验证 JWT Token，
 * 校验通过后将 userId、username 写入 Header 传递给下游服务。
 *
 * @author oa-cloud
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Value("${oa.jwt.expiration}")
    private Long tokenExpiration;

    private final GatewayWhitelistProperties gatewayWhitelistProperties;
    private final JwtUtils jwtUtils;
    private final CacheService cacheService;

    /**
     * 过滤器逻辑
     * 网关层校验token，解析token信息，包括
     * @param exchange 网关上下文
     * @param chain    过滤器链
     * @return Mono<Void>
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 1. 白名单直接放行
        if (isWhiteListed(path) || isSwaggerPath(path)) {
            return chain.filter(exchange);
        }

        // 2. 获取 Token
        String authHeader = request.getHeaders().getFirst(CommonConstant.TOKEN_HEADER);
        if (authHeader == null || !authHeader.startsWith(CommonConstant.TOKEN_PREFIX)) {
            exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(ResponseStatusEnum.UNAUTHORIZED.getCode()));
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        // 3. 使用 JwtUtils 解析 Token
        try {
            if (!jwtUtils.validateToken(token)) {
                // 无效token，只校验json，不校验有效期
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            // token续签，更新redis 中的 token过期时间
            String tokenKey = CommonConstant.USER_TOKEN_CACHE_PREFIX + token;
            cacheService.updateExpire(tokenKey, tokenExpiration, TimeUnit.SECONDS);

            ServerHttpRequest.Builder requestBuilder = request.mutate()
                    .header(CommonConstant.HEADER_USER_ID, jwtUtils.getUserId(token).toString())
                    .header(CommonConstant.HEADER_USERNAME, jwtUtils.getUsername(token));

            Long deptId = jwtUtils.getDeptId(token);
            if (deptId != null) {
                requestBuilder.header(CommonConstant.HEADER_DEPT_ID, deptId.toString());
            }
            List<String> roles = jwtUtils.getRoles(token);
            if (roles != null) {
                String rolesJson = roles.stream()
                        .map(r -> "\"" + r + "\"")
                        .collect(java.util.stream.Collectors.joining(",", "[", "]"));
                requestBuilder.header(CommonConstant.HEADER_ROLES, rolesJson);
            }

            return chain.filter(exchange.mutate().request(requestBuilder.build()).build());

        } catch (Exception e) {
            log.warn("Token 校验失败: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -100; // 优先级最高
    }

    private boolean isWhiteListed(String path) {
        return gatewayWhitelistProperties.getWhitelist().stream()
                .anyMatch(path::startsWith);
    }

    private boolean isSwaggerPath(String path) {
        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/webjars/");
    }
}
