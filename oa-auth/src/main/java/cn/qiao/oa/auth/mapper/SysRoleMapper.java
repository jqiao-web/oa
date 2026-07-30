package cn.qiao.oa.auth.mapper;

import cn.qiao.oa.auth.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色 Mapper
 * <p>
 * 单表查询已迁移至 {@link cn.qiao.oa.auth.service.SysRoleService}（LambdaQueryWrapper），
 * 此处仅保留涉及多表 JOIN 的查询。
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户 ID 查询角色（role JOIN user_role）
     */
    List<SysRole> selectByUserId(@Param("userId") Long userId);
}
