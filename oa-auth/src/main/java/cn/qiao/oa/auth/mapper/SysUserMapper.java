package cn.qiao.oa.auth.mapper;

import cn.qiao.oa.auth.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户 Mapper
 * <p>
 * 单表查询已迁移至 {@link cn.qiao.oa.auth.service.SysUserService}（LambdaQueryWrapper），
 * 此处仅保留动态条件分页等不便用 Wrapper 表达的查询。
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    IPage<SysUser> selectPageList(Page<SysUser> page,
                                  @Param("username") String username,
                                  @Param("deptId") Long deptId,
                                  @Param("status") Integer status);
}
