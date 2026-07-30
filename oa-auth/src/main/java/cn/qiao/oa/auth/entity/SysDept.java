package cn.qiao.oa.auth.entity;

import cn.qiao.oa.common.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
public class SysDept extends BaseEntity {

    private Long parentId;

    private String name;

    private Integer sort;

    private Long leaderId;

    private Integer status;
}
