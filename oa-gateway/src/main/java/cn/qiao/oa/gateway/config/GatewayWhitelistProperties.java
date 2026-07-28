package cn.qiao.oa.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "gateway-whitelist")
public class GatewayWhitelistProperties {

    /** 白名单路径（免鉴权） */
    private List<String> whitelist = new ArrayList<>();
}
