package cn.qiao.oa.auth.entity;

import cn.qiao.oa.common.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends BaseEntity {

    private Long parentId;

    private String name;

    private Integer type;

    private String path;

    private String component;

    private String permission;

    private String icon;

    private Integer sort;

    private Integer visible;

    @TableField(exist = false)
    private String[] roleIds;
}
