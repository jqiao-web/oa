package cn.qiao.oa.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatusEnum {
    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    UNAUTHORIZED(401, "未登录，请先登录"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "未找到该资源"),
    ACCOUNT_NOT_EXIST(405, "账号不存在"),
    PASSWORD_ERROR(406, "密码错误"),
    ACCOUNT_LOCKED(407, "账号被锁定"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    GATEWAY_TIMEOUT(504, "网关超时");

    private final int code;
    private final String message;
}
