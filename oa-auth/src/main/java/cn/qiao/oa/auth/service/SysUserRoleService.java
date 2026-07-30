package cn.qiao.oa.auth.service;

import cn.qiao.oa.auth.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户角色关联服务接口
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 根据用户 ID 删除关联
     */
    boolean removeByUserId(Long userId);

    /**
     * 根据用户 ID 列表批量删除关联
     */
    boolean removeByUserIds(List<Long> userIds);

    /**
     * 根据用户 ID 查询关联
     */
    List<SysUserRole> listByUserId(Long userId);
}
