package cn.qiao.oa.common.security.utils;

import lombok.Data;

/**
 * 当前登录用户上下文（基于 ThreadLocal）
 */
public class SecurityUtils {

    private static final ThreadLocal<LoginUser> USER_HOLDER = new ThreadLocal<>();

    public static void setLoginUser(LoginUser loginUser) {
        USER_HOLDER.set(loginUser);
    }

    public static LoginUser getLoginUser() {
        return USER_HOLDER.get();
    }

    public static Long getUserId() {
        LoginUser user = getLoginUser();
        return user != null ? user.getUserId() : null;
    }

    public static String getUsername() {
        LoginUser user = getLoginUser();
        return user != null ? user.getUsername() : null;
    }

    public static void clear() {
        USER_HOLDER.remove();
    }

    @Data
    public static class LoginUser {
        private Long userId;
        private String username;
        private Long deptId;
        private java.util.List<String> permissions;
        private java.util.List<String> roles;
        private Integer dataScope; // 数据权限范围
    }
}
