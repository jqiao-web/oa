package cn.qiao.oa.auth.service;

import cn.qiao.oa.auth.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色服务接口
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据角色编码查询角色
     */
    SysRole getByCode(String code);

    /**
     * 根据状态查询角色列表
     */
    List<SysRole> listByStatus(Integer status);

    /**
     * 根据用户 ID 查询角色列表（多表联查）
     */
    List<SysRole> listByUserId(Long userId);
}
