package cn.qiao.oa.common.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 审批通知消息 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalNotifyMessage implements Serializable {

    /** 操作类型：submit/approve/reject/withdraw/urge */
    private String action;

    /** 审批单 ID */
    private Long instanceId;

    /** 目标用户 ID（接收通知的人） */
    private Long targetUserId;

    /** 通知标题 */
    private String title;
}
