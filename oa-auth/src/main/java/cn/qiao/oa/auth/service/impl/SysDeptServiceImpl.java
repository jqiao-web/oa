package cn.qiao.oa.auth.service.impl;

import cn.qiao.oa.auth.entity.SysDept;
import cn.qiao.oa.auth.mapper.SysDeptMapper;
import cn.qiao.oa.auth.service.SysDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门服务实现
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Override
    public List<SysDept> listAll() {
        return lambdaQuery()
                .orderByAsc(SysDept::getSort)
                .list();
    }

    @Override
    public List<SysDept> listByParentId(Long parentId) {
        return lambdaQuery()
                .eq(SysDept::getParentId, parentId)
                .orderByAsc(SysDept::getSort)
                .list();
    }

    @Override
    public List<Long> listChildIds(Long parentId) {
        List<SysDept> depts = lambdaQuery()
                .select(SysDept::getId)
                .eq(SysDept::getParentId, parentId)
                .list();
        return depts.stream()
                .map(SysDept::getId)
                .collect(Collectors.toList());
    }
}
