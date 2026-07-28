package cn.qiao.oa.common.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 序列化配置
 * <p>
 * 解决 RedisTemplate 默认使用 JDK 序列化导致的可读性差问题。
 * <ul>
 *     <li>Key 使用 String 序列化</li>
 *     <li>Value 使用 Jackson JSON 序列化（支持 LocalDateTime 等 Java 8 时间类型）</li>
 * </ul>
 *
 * @author oa-cloud
 */
@Configuration
@SuppressWarnings({"deprecation", "removal"})
public class RedisTemplateConfig {

    /**
     * 自定义 RedisTemplate
     * <p>
     * 使用 StringRedisSerializer 序列化 Key，
     * 使用 GenericJackson2JsonRedisSerializer 序列化 Value，
     * 保证 Redis 中存储的数据可读且支持复杂对象反序列化。
     *
     * @param connectionFactory Redis 连接工厂（Spring Boot 自动注入）
     * @return 配置完成的 RedisTemplate 实例
     */
    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RedisTemplate<String, Object> redisTemplate(@Autowired RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Jackson 序列化配置
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL
        );
        // 支持 Java 8 时间类型（LocalDateTime 等）
        objectMapper.registerModule(new JavaTimeModule());

        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        // Key 序列化 - String
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // Value 序列化 - JSON
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
