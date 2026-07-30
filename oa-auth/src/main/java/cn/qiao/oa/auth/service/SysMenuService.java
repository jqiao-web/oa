package cn.qiao.oa.auth.service;

import cn.qiao.oa.auth.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 查询全部菜单（按 sort 排序）
     */
    List<SysMenu> listAll();

    /**
     * 根据父级 ID 查询子菜单
     */
    List<SysMenu> listByParentId(Long parentId);

    /**
     * 根据角色 ID 查询菜单（多表联查）
     */
    List<SysMenu> listByRoleId(Long roleId);

    /**
     * 根据角色 ID 列表查询权限标识（多表联查）
     */
    List<String> listPermissionsByRoleIds(List<Long> roleIds);

    /**
     * 根据角色 ID 列表查询菜单树（多表联查）
     */
    List<SysMenu> listMenuTreeByRoleIds(List<Long> roleIds);
}
