package cn.qiao.oa.common.web.interceptor;

import cn.qiao.oa.common.core.constant.CommonConstant;
import cn.qiao.oa.common.redis.cache.CacheService;
import cn.qiao.oa.common.web.utils.SecurityUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final CacheService cacheService;
    private final ObjectMapper objectMapper;

    /**
     * WebMVC拦截所有请求，接收网关层的用户信息设置用户上下文
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userIdStr = request.getHeader(CommonConstant.HEADER_USER_ID);
        String username = request.getHeader(CommonConstant.HEADER_USERNAME);

        if (userIdStr != null && username != null) {
            SecurityUtils.LoginUser loginUser = new SecurityUtils.LoginUser();
            loginUser.setUserId(Long.parseLong(userIdStr));
            loginUser.setUsername(username);

            String deptIdStr = request.getHeader(CommonConstant.HEADER_DEPT_ID);
            if (deptIdStr != null) {
                loginUser.setDeptId(Long.parseLong(deptIdStr));
            }

            loginUser.setRoles(
                    parseRoles(request.getHeader(CommonConstant.HEADER_ROLES))
            );

            List<String> permissions = loadPermissions(loginUser.getUserId());
            loginUser.setPermissions(permissions);

            SecurityUtils.setLoginUser(loginUser);
            log.debug("用户上下文已设置: userId={}, username={}, roles={}, permissions={}",
                    loginUser.getUserId(), loginUser.getUsername(), loginUser.getRoles(), loginUser.getPermissions());
        }

        return true;
    }

    private List<String> parseRoles(String rolesHeader) {
        if (rolesHeader == null || rolesHeader.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(rolesHeader, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            log.warn("解析角色列表失败: {}", rolesHeader);
            return new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> loadPermissions(Long userId) {
        try {
            String cacheKey = CommonConstant.PERMISSIONS_CACHE_KEY_PREFIX + userId;
            List<String> cached = cacheService.get(cacheKey, List.class,
                    () -> null);
            return cached != null ? cached : new ArrayList<>();
        } catch (Exception e) {
            log.debug("加载权限缓存失败（Redis 可能不可用）: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        SecurityUtils.clear();
    }
}
