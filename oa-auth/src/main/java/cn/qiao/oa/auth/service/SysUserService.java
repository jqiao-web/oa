package cn.qiao.oa.auth.service;

import cn.qiao.oa.auth.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户服务接口
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据用户名查询用户
     */
    SysUser getByUsername(String username);

    /**
     * 根据部门 ID 列表查询用户
     */
    List<SysUser> listByDeptIds(List<Long> deptIds);
}
