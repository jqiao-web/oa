package cn.qiao.oa.common.jwt.utils;

import cn.qiao.oa.common.core.constant.CommonConstant;
import cn.qiao.oa.common.redis.cache.CacheService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JWT 工具类
 * <p>
 * 提供 JWT Token 的生成、解析、验证功能。
 * 密钥和过期时间通过配置文件注入（{@code oa.jwt.secret} / {@code oa.jwt.expiration}）。
 *
 * @author oa-cloud
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {
    private final CacheService cacheService;

    @Value("${oa.jwt.secret:oa-cloud-jwt-secret-key-must-be-at-least-256-bits-long}")
    private String secret;

    @Value("${oa.jwt.expiration:86400000}")
    private Long expiration; // 默认 24 小时

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /** 生成 JWT Token */
    public String generateToken(Long userId, String username) {
        return generateToken(userId, username, null, null);
    }

    /** 生成 JWT Token（含部门和角色信息） */
    public String generateToken(Long userId, String username, Long deptId, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        if (deptId != null) {
            claims.put("deptId", deptId);
        }
        if (roles != null && !roles.isEmpty()) {
            claims.put("roles", roles);
        }

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /** 解析 Token */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /** 从 Token 获取 userId */
    public Long getUserId(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    /** 从 Token 获取 username */
    public String getUsername(String token) {
        return parseToken(token).getSubject();
    }

    /** 从 Token 获取 deptId */
    public Long getDeptId(String token) {
        Claims claims = parseToken(token);
        Object deptId = claims.get("deptId");
        return deptId != null ? Long.valueOf(deptId.toString()) : null;
    }

    /** 从 Token 获取 roles */
    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        Claims claims = parseToken(token);
        return claims.get("roles", List.class);
    }

    /** 验证 Token 是否有效 */
    public boolean validateToken(String token) {
        try {
            // 1、解析token
            // 不校验jwt有效期
            Claims claims = parseToken(token);
            // 2、校验redis 判断token是否过期
            String tokenKey = CommonConstant.USER_TOKEN_CACHE_PREFIX + token;
            Object tokenValue = cacheService.get(tokenKey, String.class, () -> null);
            // return !claims.getExpiration().before(new Date());
            return tokenValue != null;
        } catch (Exception e) {
            log.info("Token 校验失败: {}", e.getMessage());
            return false;
        }
    }
}
