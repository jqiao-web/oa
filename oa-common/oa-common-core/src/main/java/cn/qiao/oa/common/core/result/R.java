package cn.qiao.oa.common.core.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果封装
 * <p>
 * 所有接口统一返回此对象，保证前后端接口响应格式一致。
 * <ul>
 *     <li>code - 状态码（200=成功，其他=失败）</li>
 *     <li>message - 提示信息</li>
 *     <li>data - 响应数据</li>
 * </ul>
 *
 * <h3>使用示例：</h3>
 * <pre>{@code
 * // 成功响应
 * return R.ok(userList);
 * // 失败响应
 * return R.fail("操作失败");
 * // 自定义状态码
 * return R.fail(401, "未登录");
 * }</pre>
 *
 * @param <T> 响应数据泛型
 * @author oa-cloud
 */
@Data
public class R<T> implements Serializable {

    /** 状态码（200=成功，其他=失败） */
    private Integer code;
    /** 提示信息 */
    private String message;
    /** 响应数据 */
    private T data;

    private R() {}

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMessage("操作成功");
        r.setData(data);
        return r;
    }

    public static <T> R<T> ok(String message, T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static <T> R<T> fail(String message) {
        return fail(500, message);
    }

    public static <T> R<T> fail(Integer code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }
}
