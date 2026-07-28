package cn.qiao.oa.common.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 数据权限过滤注解
 * <p>
 * 标注在 Service 方法上，配合 {@code DataScopeAspect} 自动拼接数据权限 SQL 条件。
 * 根据当前用户的数据权限范围，自动过滤可见数据。
 *
 * <h3>数据权限范围（dataScope）：</h3>
 * <ul>
 *     <li>1 - 全部数据（超级管理员）</li>
 *     <li>2 - 本部门数据</li>
 *     <li>3 - 本部门及子部门数据</li>
 *     <li>4 - 仅本人数据</li>
 * </ul>
 *
 * <h3>使用示例：</h3>
 * <pre>{@code
 * @DataScope(deptAlias = "d", userAlias = "u")
 * public List<ApprovalInstance> selectList() { ... }
 * }</pre>
 *
 * @author oa-cloud
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 部门表的别名（SQL 中的表别名）
     * <p>例如：表 sys_dept 在 SQL 中别名为 d，则填 "d"</p>
     */
    String deptAlias() default "";

    /**
     * 用户表的别名（SQL 中的表别名）
     * <p>例如：表 sys_user 在 SQL 中别名为 u，则填 "u"</p>
     */
    String userAlias() default "";
}
