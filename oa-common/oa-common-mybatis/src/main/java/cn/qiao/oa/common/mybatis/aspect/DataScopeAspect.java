package cn.qiao.oa.common.mybatis.aspect;

import cn.qiao.oa.common.mybatis.annotation.DataScope;
import cn.qiao.oa.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 数据权限过滤切面
 * <p>
 * 拦截标注了 {@link DataScope} 注解的 Service 方法，
 * 根据当前用户的数据权限范围（dataScope），动态拼接 SQL WHERE 条件，
 * 实现行级数据权限控制。
 *
 * <h3>过滤规则：</h3>
 * <ul>
 *     <li><b>dataScope=1（全部）</b>：不拼接任何条件</li>
 *     <li><b>dataScope=2（本部门）</b>：拼接 dept_id = 当前用户部门 ID</li>
 *     <li><b>dataScope=3（本部门及子部门）</b>：拼接 dept_id IN (子部门列表)</li>
 *     <li><b>dataScope=4（仅本人）</b>：拼接 user_id = 当前用户 ID</li>
 * </ul>
 *
 * @author oa-cloud
 * @see DataScope
 */
@Slf4j
@Aspect
@Component
public class DataScopeAspect {

    /** 全部数据权限 */
    private static final int DATA_SCOPE_ALL = 1;

    /** 本部门数据权限 */
    private static final int DATA_SCOPE_DEPT = 2;

    /** 本部门及子部门数据权限 */
    private static final int DATA_SCOPE_DEPT_AND_CHILD = 3;

    /** 仅本人数据权限 */
    private static final int DATA_SCOPE_SELF = 4;

    /**
     * 方法执行前拼接数据权限 SQL 条件
     * <p>
     * 从 {@link SecurityUtils} 获取当前登录用户的数据权限范围，
     * 将拼接的 SQL 条件存入 ThreadLocal，供 Mapper 层使用。
     *
     * @param joinPoint 切入点
     * @param dataScope 数据权限注解
     */
    @Before("@annotation(dataScope)")
    public void doBefore(JoinPoint joinPoint, DataScope dataScope) {
        SecurityUtils.LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            log.warn("数据权限过滤：用户未登录，跳过");
            return;
        }

        Integer dataScopeValue = loginUser.getDataScope();
        if (dataScopeValue == null || dataScopeValue == DATA_SCOPE_ALL) {
            // 超级管理员或全部权限，不需要过滤
            return;
        }

        String deptAlias = dataScope.deptAlias();
        String userAlias = dataScope.userAlias();
        StringBuilder sqlString = new StringBuilder();

        switch (dataScopeValue) {
            case DATA_SCOPE_DEPT:
                // 本部门
                if (!deptAlias.isEmpty()) {
                    sqlString.append(String.format(" AND %s.dept_id = %d",
                            deptAlias, loginUser.getDeptId()));
                }
                break;

            case DATA_SCOPE_DEPT_AND_CHILD:
                // 本部门及子部门（实际项目中需要查询子部门列表或使用 FIND_IN_SET）
                if (!deptAlias.isEmpty()) {
                    sqlString.append(String.format(
                            " AND (%s.dept_id = %d OR %s.dept_id IN (SELECT id FROM sys_dept WHERE FIND_IN_SET(%d, ancestors)))",
                            deptAlias, loginUser.getDeptId(),
                            deptAlias, loginUser.getDeptId()));
                }
                break;

            case DATA_SCOPE_SELF:
                // 仅本人
                if (!userAlias.isEmpty()) {
                    sqlString.append(String.format(" AND %s.id = %d",
                            userAlias, loginUser.getUserId()));
                }
                break;

            default:
                break;
        }

        if (sqlString.length() > 0) {
            // 将数据权限条件存入 ThreadLocal，供 Mapper 层通过 MyBatis 拦截器拼接
            DataScopeContextHolder.set(sqlString.toString());
            log.debug("数据权限过滤条件: {}", sqlString);
        }
    }
}
