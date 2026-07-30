package cn.qiao.oa.auth.service;

import cn.qiao.oa.auth.entity.SysDept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 部门服务接口
 */
public interface SysDeptService extends IService<SysDept> {

    /**
     * 查询全部部门
     */
    List<SysDept> listAll();

    /**
     * 根据父级 ID 查询子部门
     */
    List<SysDept> listByParentId(Long parentId);

    /**
     * 根据父级 ID 查询子部门 ID 列表
     */
    List<Long> listChildIds(Long parentId);
}
