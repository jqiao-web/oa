package cn.qiao.oa.auth.entity;

import cn.qiao.oa.common.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends BaseEntity {

    private String name;

    private String code;

    private Integer dataScope;

    private Integer sort;

    private Integer status;
}
