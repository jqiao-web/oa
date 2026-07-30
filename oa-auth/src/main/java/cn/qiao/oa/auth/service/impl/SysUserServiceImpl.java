package cn.qiao.oa.auth.service.impl;

import cn.qiao.oa.auth.entity.SysUser;
import cn.qiao.oa.auth.mapper.SysUserMapper;
import cn.qiao.oa.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务实现
 */
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
