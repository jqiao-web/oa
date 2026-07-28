package cn.qiao.oa.common.security.annotation;

import java.lang.annotation.*;

/**
 * 接口权限校验注解
 * 使用示例: @RequiresPermission("approval:audit:approve")
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermission {
    /** 权限标识，如 approval:audit:approve */
    String value();
}
