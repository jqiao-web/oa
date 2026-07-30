package cn.qiao.oa.auth.service.impl;

import cn.qiao.oa.auth.entity.SysRole;
import cn.qiao.oa.auth.mapper.SysRoleMapper;
import cn.qiao.oa.auth.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色服务实现
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public SysRole getByCode(String code) {
        return lambdaQuery()
                .eq(SysRole::getCode, code)
                .one();
    }

    @Override
    public List<SysRole> listByStatus(Integer status) {
        return lambdaQuery()
                .eq(status != null, SysRole::getStatus, status)
                .orderByAsc(SysRole::getSort)
                .list();
    }

    @Override
    public List<SysRole> listByUserId(Long userId) {
        // 多表联查，委托给 Mapper
        return getBaseMapper().selectByUserId(userId);
    }
}
