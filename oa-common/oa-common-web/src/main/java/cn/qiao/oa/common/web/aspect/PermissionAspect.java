package cn.qiao.oa.common.web.aspect;

import cn.qiao.oa.common.core.constant.CommonConstant;
import cn.qiao.oa.common.core.enums.ResponseStatusEnum;
import cn.qiao.oa.common.core.exception.BusinessException;
import cn.qiao.oa.common.web.annotation.RequiresPermission;
import cn.qiao.oa.common.web.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 权限校验切面
 */
@Slf4j
@Aspect
@Component
public class PermissionAspect {

    @Before("@annotation(requiresPermission)")
    public void checkPermission(JoinPoint joinPoint, RequiresPermission requiresPermission) {
        String permission = requiresPermission.value();

        SecurityUtils.LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            throw new BusinessException(ResponseStatusEnum.UNAUTHORIZED.getCode(), ResponseStatusEnum.UNAUTHORIZED.getMessage());
        }

        // 超级管理员拥有所有权限
        if (loginUser.getRoles() != null && loginUser.getRoles().contains(CommonConstant.SUPER_ADMIN)) {
            return;
        }

        // 检查是否拥有该权限
        if (loginUser.getPermissions() == null || !loginUser.getPermissions().contains(permission)) {
            log.warn("用户 {} 无权限: {}", loginUser.getUsername(), permission);
            throw new BusinessException(ResponseStatusEnum.FORBIDDEN.getCode(), ResponseStatusEnum.FORBIDDEN.getMessage());
        }
    }
}
