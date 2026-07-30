package cn.qiao.oa.auth.service.impl;

import cn.qiao.oa.auth.entity.SysUserRole;
import cn.qiao.oa.auth.mapper.SysUserRoleMapper;
import cn.qiao.oa.auth.service.SysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户角色关联服务实现
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Override
    public boolean removeByUserId(Long userId) {
        return lambdaUpdate()
                .eq(SysUserRole::getUserId, userId)
                .remove();
    }

    @Override
    public boolean removeByUserIds(List<Long> userIds) {
        return lambdaUpdate()
                .in(SysUserRole::getUserId, userIds)
                .remove();
    }

    @Override
    public List<SysUserRole> listByUserId(Long userId) {
        return lambdaQuery()
                .eq(SysUserRole::getUserId, userId)
                .list();
    }
}
