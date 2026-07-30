package cn.qiao.oa.auth.mapper;

import cn.qiao.oa.auth.entity.SysDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门 Mapper
 * <p>
 * 所有查询已迁移至 {@link cn.qiao.oa.auth.service.SysDeptService}（LambdaQueryWrapper），
 * 此处仅保留 BaseMapper 通用方法。
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {
}
