package cn.qiao.oa.common.feign.decoder;

import cn.qiao.oa.common.core.exception.BusinessException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Feign 统一错误解码器
 * <p>
 * 当被调用服务返回非 2xx 响应时，将 HTTP 错误转换为 {@link BusinessException}，
 * 保持与本地调用一致的异常处理逻辑。
 * <p>
 * 特殊状态码处理：
 * <ul>
 *     <li><b>401</b> - 认证失败，抛出 401 业务异常</li>
 *     <li><b>403</b> - 权限不足，抛出 403 业务异常</li>
 *     <li><b>404</b> - 服务或资源不存在，抛出 404 业务异常</li>
 *     <li><b>500</b> - 远程服务内部错误，抛出 500 业务异常</li>
 *     <li><b>其他</b> - 统一抛出远程调用异常</li>
 * </ul>
 *
 * @author oa-cloud
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();
        String reason = response.reason();
        String body = readBody(response);

        log.error("Feign 调用异常: method={}, status={}, reason={}, body={}",
                methodKey, status, reason, body);

        return switch (status) {
            case 401 -> new BusinessException(401, "远程服务认证失败");
            case 403 -> new BusinessException(403, "远程服务权限不足");
            case 404 -> new BusinessException(404, "远程服务或资源不存在");
            case 500 -> new BusinessException(500, "远程服务内部错误: " + truncate(body, 200));
            default -> new BusinessException(500, "远程服务调用失败 [" + status + "]: " + truncate(body, 200));
        };
    }

    /**
     * 读取响应体内容
     */
    private String readBody(Response response) {
        try {
            if (response.body() != null) {
                return Util.toString(response.body().asReader(java.nio.charset.StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            log.warn("读取 Feign 错误响应体失败: {}", e.getMessage());
        }
        return "";
    }

    /**
     * 截断字符串，防止日志或异常消息过长
     */
    private String truncate(String text, int maxLength) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text.length() > maxLength ? text.substring(0, maxLength) + "..." : text;
    }
}
