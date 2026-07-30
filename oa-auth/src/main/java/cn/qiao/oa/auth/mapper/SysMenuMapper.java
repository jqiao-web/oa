package cn.qiao.oa.auth.mapper;

import cn.qiao.oa.auth.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单 Mapper
 * <p>
 * 单表查询已迁移至 {@link cn.qiao.oa.auth.service.SysMenuService}（LambdaQueryWrapper），
 * 此处仅保留涉及多表 JOIN 的查询。
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据角色 ID 查询菜单（menu JOIN role_menu）
     */
    List<SysMenu> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色 ID 列表查询权限标识（menu JOIN role_menu JOIN role）
     */
    List<String> selectPermissionsByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据角色 ID 列表查询菜单树（menu JOIN role_menu JOIN role）
     */
    List<SysMenu> selectMenuTreeByRoleIds(@Param("roleIds") List<Long> roleIds);
}
