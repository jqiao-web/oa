package cn.qiao.oa.common.mybatis.handler;

import cn.qiao.oa.common.web.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充处理器
 * <p>
 * 在实体执行 INSERT 或 UPDATE 操作时，自动填充以下公共字段：
 * <ul>
 *     <li><b>INSERT</b>：createTime、updateTime、createBy、updateBy</li>
 *     <li><b>UPDATE</b>：updateTime、updateBy</li>
 * </ul>
 * <p>
 * createBy 和 updateBy 从 {@link SecurityUtils} 的 ThreadLocal 上下文中获取当前登录用户 ID。
 * 如果用户未登录（如系统定时任务场景），则不填充操作人字段。
 *
 * @author oa-cloud
 * @see com.baomidou.mybatisplus.annotation.FieldFill
 */
@Slf4j
@Component
public class AutoFillHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充
     * <p>
     * 填充字段：createTime（当前时间）、updateTime（当前时间）、
     * createBy（当前用户 ID）、updateBy（当前用户 ID）
     *
     * @param metaObject MyBatis 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("自动填充 INSERT 字段: {}", metaObject.getOriginalObject().getClass().getSimpleName());

        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);

        // 填充操作人
        Long userId = getCurrentUserId();
        if (userId != null) {
            this.strictInsertFill(metaObject, "createBy", () -> userId, Long.class);
            this.strictInsertFill(metaObject, "updateBy", () -> userId, Long.class);
        }
    }

    /**
     * 更新时自动填充
     * <p>
     * 填充字段：updateTime（当前时间）、updateBy（当前用户 ID）
     *
     * @param metaObject MyBatis 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("自动填充 UPDATE 字段: {}", metaObject.getOriginalObject().getClass().getSimpleName());

        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);

        Long userId = getCurrentUserId();
        if (userId != null) {
            this.strictUpdateFill(metaObject, "updateBy", () -> userId, Long.class);
        }
    }

    /**
     * 获取当前登录用户 ID
     *
     * @return 用户 ID，未登录时返回 null
     */
    private Long getCurrentUserId() {
        try {
            return SecurityUtils.getUserId();
        } catch (Exception e) {
            log.debug("获取当前用户 ID 失败（可能为系统任务场景）");
            return null;
        }
    }
}
