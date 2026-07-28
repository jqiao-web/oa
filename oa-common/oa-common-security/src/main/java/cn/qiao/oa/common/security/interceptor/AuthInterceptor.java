package cn.qiao.oa.common.security.interceptor;

import cn.qiao.oa.common.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用户认证拦截器
 * <p>
 * 从 Gateway 传递的 HTTP Header 中提取用户信息，写入 {@link SecurityUtils} 的 ThreadLocal 上下文中，
 * 供后续 Controller / Service 层通过 {@code SecurityUtils.getUserId()} 等方式获取当前登录用户。
 * <p>
 * Gateway 鉴权通过后，会通过 Header 向下传递以下字段：
 * <ul>
 *     <li>{@code X-User-Id} - 用户 ID</li>
 *     <li>{@code X-Username} - 用户名</li>
 *     <li>{@code X-Dept-Id} - 部门 ID（可选）</li>
 *     <li>{@code X-Data-Scope} - 数据权限范围（可选）</li>
 * </ul>
 *
 * @author oa-cloud
 * @see SecurityUtils
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    /** Gateway 传递用户 ID 的 Header 名称 */
    private static final String HEADER_USER_ID = "X-User-Id";

    /** Gateway 传递用户名的 Header 名称 */
    private static final String HEADER_USERNAME = "X-Username";

    /** Gateway 传递部门 ID 的 Header 名称 */
    private static final String HEADER_DEPT_ID = "X-Dept-Id";

    /** Gateway 传递数据权限范围的 Header 名称 */
    private static final String HEADER_DATA_SCOPE = "X-Data-Scope";

    /**
     * 请求预处理：从 Header 中提取用户信息并写入 ThreadLocal
     *
     * @param request  HTTP 请求
     * @param response HTTP 响应
     * @param handler  处理器
     * @return 始终返回 true，继续执行后续处理
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userIdStr = request.getHeader(HEADER_USER_ID);
        String username = request.getHeader(HEADER_USERNAME);

        if (userIdStr != null && username != null) {
            SecurityUtils.LoginUser loginUser = new SecurityUtils.LoginUser();
            loginUser.setUserId(Long.parseLong(userIdStr));
            loginUser.setUsername(username);

            // 可选字段：部门 ID
            String deptIdStr = request.getHeader(HEADER_DEPT_ID);
            if (deptIdStr != null) {
                loginUser.setDeptId(Long.parseLong(deptIdStr));
            }

            // 可选字段：数据权限范围
            String dataScopeStr = request.getHeader(HEADER_DATA_SCOPE);
            if (dataScopeStr != null) {
                loginUser.setDataScope(Integer.parseInt(dataScopeStr));
            }

            SecurityUtils.setLoginUser(loginUser);
            log.debug("用户上下文已设置: userId={}, username={}", loginUser.getUserId(), loginUser.getUsername());
        }

        return true;
    }

    /**
     * 请求完成后清理 ThreadLocal，防止内存泄漏
     *
     * @param request  HTTP 请求
     * @param response HTTP 响应
     * @param handler  处理器
     * @param ex       异常（如果有）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        SecurityUtils.clear();
    }
}
