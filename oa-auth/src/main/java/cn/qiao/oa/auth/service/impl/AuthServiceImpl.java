package cn.qiao.oa.auth.service.impl;

import cn.qiao.oa.auth.dto.*;
import cn.qiao.oa.auth.entity.*;
import cn.qiao.oa.auth.service.*;
import cn.qiao.oa.common.core.constant.CommonConstant;
import cn.qiao.oa.common.core.enums.ResponseStatusEnum;
import cn.qiao.oa.common.core.exception.BusinessException;
import cn.qiao.oa.common.jwt.utils.JwtUtils;
import cn.qiao.oa.common.redis.cache.CacheService;
import cn.qiao.oa.common.web.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Value("${oa.jwt.expiration}")
    private Long tokenExpiration;

    private final SysUserService userService;
    private final SysDeptService deptService;
    private final SysRoleService roleService;
    private final SysMenuService menuService;
    private final SysUserRoleService userRoleService;
    private final SysRoleMenuService roleMenuService;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final CacheService cacheService;

    // 权限缓存有效期 1800 秒，即 30 分钟
    private static final long PERMISSIONS_CACHE_EXPIRE = 1800;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        log.info("用户登录: {}", loginDTO.getUsername());

        SysUser user = userService.getByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException(ResponseStatusEnum.ACCOUNT_NOT_EXIST.getCode(), ResponseStatusEnum.ACCOUNT_NOT_EXIST.getMessage());
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(ResponseStatusEnum.PASSWORD_ERROR.getCode(), ResponseStatusEnum.PASSWORD_ERROR.getMessage());
        }

        if (user.getStatus() != null && user.getStatus() == CommonConstant.USER_STATUS_DISABLE) {
            throw new BusinessException(ResponseStatusEnum.ACCOUNT_LOCKED.getCode(), ResponseStatusEnum.ACCOUNT_LOCKED.getMessage());
        }

        List<SysRole> roles = roleService.listByUserId(user.getId());
        List<String> roleCodes = roles.stream().map(SysRole::getCode).collect(Collectors.toList());
        List<Long> roleIds = roles.stream().map(SysRole::getId).collect(Collectors.toList());

        List<String> permissions = Collections.emptyList();
        if (!roleIds.isEmpty()) {
            List<String> perms = menuService.listPermissionsByRoleIds(roleIds);
            if (perms != null) {
                permissions = perms;
            }
        }

        // 缓存权限
        cacheService.put(CommonConstant.PERMISSIONS_CACHE_KEY_PREFIX + user.getId(), permissions, PERMISSIONS_CACHE_EXPIRE);

        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getDeptId(), roleCodes);
        // 缓存用户信息
        cacheService.put(CommonConstant.USER_TOKEN_CACHE_PREFIX + token, user.getId(), tokenExpiration);

        UserInfoVO userInfo = buildUserInfo(user, roles, permissions);

        return LoginVO.builder()
                .token(token)
                .expiresIn(tokenExpiration)
                .userInfo(userInfo)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO registerDTO) {
        log.info("用户注册: {}", registerDTO.getUsername());

        SysUser existUser = userService.getByUsername(registerDTO.getUsername());
        if (existUser != null) {
            throw new BusinessException(400, "账号已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRealName(registerDTO.getRealName());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setStatus(CommonConstant.STATUS_ENABLE);
        userService.save(user);

        SysRole defaultRole = roleService.getByCode("ROLE_USER");
        if (defaultRole != null) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(defaultRole.getId());
            userRoleService.save(userRole);
        }
    }

    @Override
    public void logout(String token) {
        log.info("用户登出: {}", token);
        SecurityUtils.LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null) {
            // 清空缓存
            cacheService.evict(CommonConstant.PERMISSIONS_CACHE_KEY_PREFIX + loginUser.getUserId());
            cacheService.evict(CommonConstant.USER_TOKEN_CACHE_PREFIX + token);
        }
        SecurityUtils.clear();
    }

    @Override
    public UserInfoVO getUserInfo() {
        SecurityUtils.LoginUser loginUser = SecurityUtils.getLoginUser();

        SysUser user = userService.getById(loginUser.getUserId());

        List<SysRole> roles = roleService.listByUserId(user.getId());
        List<Long> roleIds = roles.stream().map(SysRole::getId).toList();
        List<String> roleCodes = roles.stream().map(SysRole::getCode).toList();

        List<String> permissions = new ArrayList<>();
        try {
            String cacheKey = CommonConstant.PERMISSIONS_CACHE_KEY_PREFIX + user.getId();
            List<String> cached = cacheService.get(cacheKey, List.class, PERMISSIONS_CACHE_EXPIRE,
                    () -> {
                        if (!roleIds.isEmpty()) {
                            List<String> perms = menuService.listPermissionsByRoleIds(roleIds);
                            return perms != null ? perms : Collections.emptyList();
                        }
                        return Collections.emptyList();
                    });
            if (cached != null) {
                permissions = cached;
            }
        } catch (Exception e) {
            log.warn("获取权限缓存失败: {}", e.getMessage());
        }

        List<MenuVO> menus = buildMenuTree(roleIds);

        return buildUserInfo(user, roles, permissions, menus);
    }

    private UserInfoVO buildUserInfo(SysUser user, List<SysRole> roles, List<String> permissions) {
        return buildUserInfo(user, roles, permissions, null);
    }

    private UserInfoVO buildUserInfo(SysUser user, List<SysRole> roles, List<String> permissions, List<MenuVO> menus) {
        String deptName = null;
        if (user.getDeptId() != null) {
            SysDept dept = deptService.getById(user.getDeptId());
            if (dept != null) {
                deptName = dept.getName();
            }
        }

        List<String> roleCodes = roles.stream().map(SysRole::getCode).collect(Collectors.toList());

        return UserInfoVO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .avatar(user.getAvatar())
                .deptId(user.getDeptId())
                .deptName(deptName)
                .roles(roleCodes)
                .permissions(permissions)
                .menus(menus)
                .build();
    }

    private List<MenuVO> buildMenuTree(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<SysMenu> menus = menuService.listMenuTreeByRoleIds(roleIds);
        if (menus == null || menus.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Long, MenuVO> menuMap = menus.stream()
                .collect(Collectors.toMap(SysMenu::getId, this::convertToMenuVO));

        List<MenuVO> roots = new ArrayList<>();
        for (MenuVO menuVO : menuMap.values()) {
            if (menuVO.getParentId() == null || menuVO.getParentId() == 0) {
                roots.add(menuVO);
            } else {
                MenuVO parent = menuMap.get(menuVO.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(menuVO);
                } else {
                    roots.add(menuVO);
                }
            }
        }

        return roots;
    }

    private MenuVO convertToMenuVO(SysMenu menu) {
        return MenuVO.builder()
                .id(menu.getId())
                .parentId(menu.getParentId())
                .name(menu.getName())
                .type(menu.getType())
                .path(menu.getPath())
                .component(menu.getComponent())
                .permission(menu.getPermission())
                .icon(menu.getIcon())
                .sort(menu.getSort())
                .visible(menu.getVisible())
                .children(new ArrayList<>())
                .build();
    }
}
