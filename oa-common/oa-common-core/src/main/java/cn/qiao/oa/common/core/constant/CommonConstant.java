package cn.qiao.oa.common.core.constant;

/**
 * 公共常量
 */
public class CommonConstant {

    /** 超级管理员角色编码 */
    public static final String SUPER_ADMIN = "super_admin";

    /** 正常状态 */
    public static final Integer STATUS_ENABLE = 1;

    /** 禁用状态 */
    public static final Integer STATUS_DISABLE = 0;

    /** 逻辑删除：未删除 */
    public static final Integer NOT_DELETED = 0;

    /** 逻辑删除：已删除 */
    public static final Integer DELETED = 1;

    /** Redis Key 前缀 */
    public static final String REDIS_PREFIX = "oa:";

    /** 用户缓存前缀 */
    public static final String USER_CACHE_PREFIX = REDIS_PREFIX + "user:";
    /** 用户token缓存前缀 */
    public static final String USER_TOKEN_CACHE_PREFIX = REDIS_PREFIX + "user:token:";
    /** 用户权限缓存前缀 */
    public static final String PERMISSIONS_CACHE_KEY_PREFIX = REDIS_PREFIX + "user:permissions:";

    /** 部门树缓存 Key */
    public static final String DEPT_TREE_CACHE_KEY = REDIS_PREFIX + "dept:tree";

    /** 菜单树缓存 Key */
    public static final String MENU_TREE_CACHE_KEY = REDIS_PREFIX + "menu:tree";

    /** Token 前缀 */
    public static final String TOKEN_PREFIX = "Bearer ";

    /** 请求头 Token 名称 */
    public static final String TOKEN_HEADER = "Authorization";

    /** 用户状态  */
    public static final int USER_STATUS_DISABLE = 0; // 禁用
    public static final int USER_STATUS_ENABLE = 1; // 启用
    public static final int USER_STATUS_QUIT = 2; // 离职

    /** 请求头用户信息参数，网关层生成，MVC拦截器获取保存到ThreadLocal  */
    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_USERNAME = "X-Username";
    public static final String HEADER_DEPT_ID = "X-Dept-Id";
    public static final String HEADER_DATA_SCOPE = "X-Data-Scope";
    public static final String HEADER_ROLES = "X-Roles";
}
