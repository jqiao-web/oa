package cn.qiao.oa.common.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 任务通知消息 DTO
 * <p>
 * 用于项目服务（oa-project）向通知服务（oa-notification）发送任务相关通知。
 * 场景包括：任务分配、任务到期提醒、任务状态变更等。
 *
 * @author oa-cloud
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskNotifyMessage implements Serializable {

    /** 操作类型：assign（分配）/ deadline（到期提醒）/ status_change（状态变更）/ comment（评论） */
    private String action;

    /** 任务 ID */
    private Long taskId;

    /** 项目 ID */
    private Long projectId;

    /** 目标用户 ID（接收通知的人） */
    private Long targetUserId;

    /** 通知标题 */
    private String title;

    /** 通知内容描述 */
    private String content;
}
