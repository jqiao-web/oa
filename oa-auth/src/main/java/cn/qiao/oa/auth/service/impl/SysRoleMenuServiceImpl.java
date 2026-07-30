package cn.qiao.oa.auth.service.impl;

import cn.qiao.oa.auth.entity.SysRoleMenu;
import cn.qiao.oa.auth.mapper.SysRoleMenuMapper;
import cn.qiao.oa.auth.service.SysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色菜单关联服务实现
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    @Override
    public boolean removeByRoleId(Long roleId) {
        return lambdaUpdate()
                .eq(SysRoleMenu::getRoleId, roleId)
                .remove();
    }

    @Override
    public boolean removeByRoleIds(List<Long> roleIds) {
        return lambdaUpdate()
                .in(SysRoleMenu::getRoleId, roleIds)
                .remove();
    }

    @Override
    public List<SysRoleMenu> listByRoleId(Long roleId) {
        return lambdaQuery()
                .eq(SysRoleMenu::getRoleId, roleId)
                .list();
    }
}
