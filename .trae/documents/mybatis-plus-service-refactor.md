# oa-auth 模块 MyBatis-Plus 服务层改造方案

## Context（背景）

当前 oa-auth 模块的 [AuthServiceImpl.java](file:///p:/cloud-project/oa-auth/src/main/java/cn/qiao/oa/auth/service/impl/AuthServiceImpl.java) 直接注入 6 个 Mapper 进行数据访问，没有独立的领域 Service 层。Mapper 中混用了 BaseMapper 通用方法和大量自定义 XML 查询（单表简单查询与多表联查混杂）。

用户要求"服务层和模型层全部使用 MyBatis-Plus"，需要：
1. 建立标准的 `IService<T>` / `ServiceImpl<M, T>` 领域 Service 体系
2. 单表查询统一用 `LambdaQueryWrapper` / `LambdaUpdateWrapper` 替代自定义 XML
3. 多表联查保留在 Mapper XML（MyBatis-Plus 不擅长复杂 JOIN）

模型层（实体类）已符合 MyBatis-Plus 规范（继承 BaseEntity + `@TableName`），无需改动。

---

## 改造步骤

### 步骤 1：创建 4 个主表领域 Service

为 4 个主表实体创建 `IService` 接口 + `ServiceImpl` 实现类，路径 `oa-auth/src/main/java/cn/qiao/oa/auth/service/`：

| Service 接口 | 实现类 | 继承 | 迁移自 Mapper 的方法（用 LambdaQueryWrapper 实现） |
|---|---|---|---|
| `SysUserService` | `SysUserServiceImpl` | `IService<SysUser>` | `getByUsername`、`listByDeptIds` |
| `SysRoleService` | `SysRoleServiceImpl` | `IService<SysRole>` | `getByCode`、`listByStatus` |
| `SysMenuService` | `SysMenuServiceImpl` | `IService<SysMenu>` | `listAll`、`listByParentId` |
| `SysDeptService` | `SysDeptServiceImpl` | `IService<SysDept>` | `listAll`、`listByParentId`、`listChildIds` |

**示例（SysUserServiceImpl）：**
```java
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public SysUser getByUsername(String username) {
        return lambdaQuery()
                .eq(SysUser::getUsername, username)
                .one();
    }

    @Override
    public List<SysUser> listByDeptIds(List<Long> deptIds) {
        return lambdaQuery()
                .in(SysUser::getDeptId, deptIds)
                .list();
    }
}
```

> 注：逻辑删除由 MyBatis-Plus 全局配置自动处理（`logic-delete-field: deleted`），无需在 Wrapper 中手动加 `deleted = 0` 条件。

### 步骤 2：创建 2 个关联表领域 Service

`SysUserRole`、`SysRoleMenu` 是纯关联表（无 id 主键），仍可继承 `IService`/`ServiceImpl`，使用 `list`/`save`/`remove`/`saveBatch` 等方法。`batchInsert` 用 `IService.saveBatch` 替代。

| Service 接口 | 实现类 | 迁移自 Mapper 的方法 |
|---|---|---|
| `SysUserRoleService` | `SysUserRoleServiceImpl` | `removeByUserId`、`removeByUserIds`、`listByUserId`（batchInsert → `saveBatch`） |
| `SysRoleMenuService` | `SysRoleMenuServiceImpl` | `removeByRoleId`、`removeByRoleIds`、`listByRoleId`（batchInsert → `saveBatch`） |

**示例（SysUserRoleServiceImpl）：**
```java
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Override
    public boolean removeByUserId(Long userId) {
        return lambdaUpdate()
                .eq(SysUserRole::getUserId, userId)
                .remove();
    }

    @Override
    public List<SysUserRole> listByUserId(Long userId) {
        return lambdaQuery()
                .eq(SysUserRole::getUserId, userId)
                .list();
    }
}
```

### 步骤 3：精简 Mapper 接口（移除已迁移方法）

已迁移到 Service 的单表查询方法从 Mapper 接口移除。**保留多表联查方法**：

| Mapper | 保留的方法（多表联查） | 移除的方法（已迁移到 Service） |
|---|---|---|
| `SysUserMapper` | `selectPageList`（动态条件分页） | `selectByUsername`、`selectByDeptIds` |
| `SysRoleMapper` | `selectByUserId`（role JOIN user_role） | `selectByCode`、`selectListByStatus` |
| `SysMenuMapper` | `selectByRoleId`、`selectPermissionsByRoleIds`、`selectMenuTreeByRoleIds`（menu JOIN role_menu JOIN role） | `selectAll`、`selectByParentId` |
| `SysDeptMapper` | — | `selectAll`、`selectByParentId`、`selectChildIds` |
| `SysUserRoleMapper` | — | `batchInsert`、`deleteByUserId`、`deleteByUserIds`、`selectByUserId` |
| `SysRoleMenuMapper` | — | `batchInsert`、`deleteByRoleId`、`deleteByRoleIds`、`selectByRoleId` |

### 步骤 4：精简 Mapper XML

对应移除方法的 SQL 从 XML 中删除，保留多表联查 SQL。对于移除全部方法的 Mapper（SysDeptMapper、SysUserRoleMapper、SysRoleMenuMapper），可删除整个 XML 文件。

### 步骤 5：改造 AuthServiceImpl

将 [AuthServiceImpl.java](file:///p:/cloud-project/oa-auth/src/main/java/cn/qiao/oa/auth/service/impl/AuthServiceImpl.java) 中注入的 6 个 Mapper 替换为 6 个 Service：

```java
// 改造前
private final SysUserMapper userMapper;
private final SysRoleMapper roleMapper;
// ...

// 改造后
private final SysUserService userService;
private final SysRoleService roleService;
private final SysMenuService menuService;
private final SysDeptService deptService;
private final SysUserRoleService userRoleService;
private final SysRoleMenuService roleMenuService;
```

调用方式对应调整，例如：
- `userMapper.selectByUsername(...)` → `userService.getByUsername(...)`
- `roleMapper.selectByUserId(...)` → `roleService.getBaseMapper().selectByUserId(...)`（多表联查保留在 Mapper）
- `userRoleMapper.insert(...)` → `userRoleService.save(...)`
- `menuMapper.selectPermissionsByRoleIds(...)` → `menuService.getBaseMapper().selectPermissionsByRoleIds(...)`

> 对于保留在 Mapper 中的多表联查方法，通过 `IService.getBaseMapper()` 调用，保持 Service 层封装。

---

## 涉及文件清单

**新建文件（12 个）：**
- `service/SysUserService.java`、`service/SysRoleService.java`、`service/SysMenuService.java`、`service/SysDeptService.java`、`service/SysUserRoleService.java`、`service/SysRoleMenuService.java`
- `service/impl/SysUserServiceImpl.java`、`service/impl/SysRoleServiceImpl.java`、`service/impl/SysMenuServiceImpl.java`、`service/impl/SysDeptServiceImpl.java`、`service/impl/SysUserRoleServiceImpl.java`、`service/impl/SysRoleMenuServiceImpl.java`

**修改文件（7 个）：**
- `mapper/SysUserMapper.java`、`mapper/SysRoleMapper.java`、`mapper/SysMenuMapper.java`、`mapper/SysDeptMapper.java`、`mapper/SysUserRoleMapper.java`、`mapper/SysRoleMenuMapper.java`
- `service/impl/AuthServiceImpl.java`

**删除文件（3 个，XML 全空时）：**
- `mapper/SysDeptMapper.xml`、`mapper/SysUserRoleMapper.xml`、`mapper/SysRoleMenuMapper.xml`

**修改 XML（3 个，保留联查 SQL）：**
- `mapper/SysUserMapper.xml`、`mapper/SysRoleMapper.xml`、`mapper/SysMenuMapper.xml`

---

## 验证方式

1. **编译验证**：`mvn clean compile -pl oa-auth -am`
2. **启动服务**：启动 oa-auth，确认无启动异常
3. **接口验证**：
   - `POST /auth/login` — 验证登录、JWT 生成、权限缓存
   - `GET /auth/userinfo` — 验证用户信息、角色、权限列表、菜单树
   - `POST /auth/register` — 验证用户创建、关联表写入
