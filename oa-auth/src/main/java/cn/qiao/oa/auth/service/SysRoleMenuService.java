package cn.qiao.oa.auth.service;

import cn.qiao.oa.auth.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色菜单关联服务接口
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 根据角色 ID 删除关联
     */
    boolean removeByRoleId(Long roleId);

    /**
     * 根据角色 ID 列表批量删除关联
     */
    boolean removeByRoleIds(List<Long> roleIds);

    /**
     * 根据角色 ID 查询关联
     */
    List<SysRoleMenu> listByRoleId(Long roleId);
}
