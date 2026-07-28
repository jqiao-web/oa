package cn.qiao.oa.common.mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 公共实体基类
 * <p>
 * 所有业务实体继承此类，统一提供主键、审计字段、逻辑删除等公共字段。
 * <ul>
 *     <li>id - 主键，使用雪花算法自动分配</li>
 *     <li>createBy - 创建人 ID（由 {@link cn.qiao.oa.common.mybatis.handler.AutoFillHandler} 自动填充）</li>
 *     <li>createTime - 创建时间（自动填充）</li>
 *     <li>updateBy - 更新人 ID（自动填充）</li>
 *     <li>updateTime - 更新时间（自动填充）</li>
 *     <li>deleted - 逻辑删除标志（0=未删除，1=已删除）</li>
 * </ul>
 *
 * <h3>使用示例：</h3>
 * <pre>{@code
 * @Data
 * @TableName("sys_user")
 * public class SysUser extends BaseEntity {
 *     private String username;
 *     private String password;
 * }
 * }</pre>
 *
 * @author oa-cloud
 */
@Data
public abstract class BaseEntity implements Serializable {

    /**
     * 主键 ID（雪花算法生成）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建人 ID（INSERT 时自动填充）
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 创建时间（INSERT 时自动填充）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人 ID（INSERT 和 UPDATE 时自动填充）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 更新时间（INSERT 和 UPDATE 时自动填充）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标志
     * <p>0 = 未删除，1 = 已删除</p>
     */
    @TableLogic
    private Integer deleted;
}
