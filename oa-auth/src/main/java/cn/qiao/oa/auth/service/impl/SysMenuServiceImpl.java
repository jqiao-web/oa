package cn.qiao.oa.auth.service.impl;

import cn.qiao.oa.auth.entity.SysMenu;
import cn.qiao.oa.auth.mapper.SysMenuMapper;
import cn.qiao.oa.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单服务实现
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> listAll() {
        return lambdaQuery()
                .orderByAsc(SysMenu::getSort)
                .list();
    }

    @Override
    public List<SysMenu> listByParentId(Long parentId) {
        return lambdaQuery()
                .eq(SysMenu::getParentId, parentId)
                .orderByAsc(SysMenu::getSort)
                .list();
    }

    @Override
    public List<SysMenu> listByRoleId(Long roleId) {
        // 多表联查，委托给 Mapper
        return getBaseMapper().selectByRoleId(roleId);
    }

    @Override
    public List<String> listPermissionsByRoleIds(List<Long> roleIds) {
        // 多表联查，委托给 Mapper
        return getBaseMapper().selectPermissionsByRoleIds(roleIds);
    }

    @Override
    public List<SysMenu> listMenuTreeByRoleIds(List<Long> roleIds) {
        // 多表联查，委托给 Mapper
        return getBaseMapper().selectMenuTreeByRoleIds(roleIds);
    }
}
