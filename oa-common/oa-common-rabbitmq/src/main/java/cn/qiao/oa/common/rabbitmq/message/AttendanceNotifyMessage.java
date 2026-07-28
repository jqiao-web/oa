package cn.qiao.oa.common.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 考勤通知消息 DTO
 * <p>
 * 用于考勤服务（oa-attendance）向通知服务（oa-notification）发送考勤相关通知。
 * 场景包括：迟到提醒、缺卡提醒、加班审批结果通知等。
 *
 * @author oa-cloud
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceNotifyMessage implements Serializable {

    /** 操作类型：late（迟到）/ absent（缺卡）/ overtime_result（加班审批结果）/ leave_result（请假结果） */
    private String action;

    /** 考勤记录 ID */
    private Long recordId;

    /** 目标用户 ID（接收通知的人） */
    private Long targetUserId;

    /** 通知标题 */
    private String title;

    /** 通知内容描述 */
    private String content;
}
