package cn.qiao.oa.common.redis.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 多级缓存服务（Caffeine 本地缓存 + Redis 分布式缓存）
 * <p>
 * 解决 Redis 缓存三大问题：
 * <ul>
 *     <li><b>缓存穿透</b>：缓存空值（NULL_PLACEHOLDER），避免恶意请求穿透到数据库</li>
 *     <li><b>缓存击穿</b>：使用 Caffeine 本地锁（synchronized）防止热点 Key 并发回源</li>
 *     <li><b>缓存雪崩</b>：Redis TTL 添加随机偏移量，避免大量 Key 同时过期</li>
 * </ul>
 *
 * <p>缓存查询流程：
 * <pre>
 * 请求 → Caffeine 本地缓存命中? → 返回
 *       ↓ 未命中
 *      Redis 缓存命中? → 写入 Caffeine → 返回
 *       ↓ 未命中
 *      synchronized 加锁 → 双重检查 → 查询数据库 → 写入 Redis + Caffeine → 返回
 * </pre>
 *
 * @author oa-cloud
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    /** 空值占位符，用于防止缓存穿透 */
    private static final String NULL_PLACEHOLDER = "NULL::PLACEHOLDER";

    /** Caffeine 本地缓存（最大 1000 个 Key，5 分钟后自动过期） */
    private final Cache<String, Object> localCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    /**
     * 多级缓存查询（核心方法）
     * <p>
     * 先从 Caffeine 本地缓存查询，未命中则从 Redis 查询，
     * 仍未命中则加锁查询数据库并回写缓存。
     *
     * @param key        缓存 Key
     * @param clazz      返回值类型（用于 Redis 反序列化）
     * @param expiration Redis 过期时间（秒）
     * @param supplier   数据库查询逻辑（缓存未命中时执行）
     * @param <T>        返回值泛型
     * @return 缓存或数据库查询结果
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz, long expiration, Supplier<T> supplier) {
        // 第一级：Caffeine 本地缓存
        Object localValue = localCache.getIfPresent(key);
        if (localValue != null) {
            if (NULL_PLACEHOLDER.equals(localValue)) {
                return null; // 缓存穿透的空值
            }
            return (T) localValue;
        }

        // 第二级：Redis 分布式缓存
        Object redisValue = redisTemplate.opsForValue().get(key);
        if (redisValue != null) {
            if (NULL_PLACEHOLDER.equals(redisValue.toString())) {
                localCache.put(key, NULL_PLACEHOLDER); // 空值也写入本地缓存
                return null;
            }
            localCache.put(key, redisValue); // 回写本地缓存
            return (T) redisValue;
        }

        // 第三级：缓存未命中，加锁查询数据库（防止缓存击穿）
        return loadDataFromDB(key, clazz, expiration, supplier);
    }

    /**
     * 多级缓存查询（使用默认过期时间 30 分钟）
     *
     * @param key      缓存 Key
     * @param clazz    返回值类型
     * @param supplier 数据库查询逻辑
     * @param <T>      返回值泛型
     * @return 缓存或数据库查询结果
     */
    public <T> T get(String key, Class<T> clazz, Supplier<T> supplier) {
        return get(key, clazz, 1800, supplier); // 默认 30 分钟
    }

    /**
     * 加锁查询数据库并回写缓存（防止缓存击穿）
     * <p>
     * 使用 synchronized 保证同一 JVM 内只有一个线程回源查询，
     * 查询完成后执行双重检查（Double-Check），避免重复写入。
     *
     * @param key        缓存 Key
     * @param clazz      返回值类型
     * @param expiration Redis 过期时间（秒）
     * @param supplier   数据库查询逻辑
     * @param <T>        返回值泛型
     * @return 数据库查询结果
     */
    @SuppressWarnings("unchecked")
    private synchronized <T> T loadDataFromDB(String key, Class<T> clazz, long expiration, Supplier<T> supplier) {
        // 双重检查：加锁后再次检查 Redis（可能其他线程已经回写）
        Object redisValue = redisTemplate.opsForValue().get(key);
        if (redisValue != null) {
            if (NULL_PLACEHOLDER.equals(redisValue.toString())) {
                return null;
            }
            localCache.put(key, redisValue);
            return (T) redisValue;
        }

        // 查询数据库
        T result = supplier.get();

        if (result != null) {
            // 缓存雪崩防护：TTL 添加随机偏移量（±20%），避免大量 Key 同时过期
            long randomOffset = (long) (expiration * 0.2 * (Math.random() * 2 - 1));
            long finalExpiration = expiration + randomOffset;

            redisTemplate.opsForValue().set(key, result, finalExpiration, TimeUnit.SECONDS);
            localCache.put(key, result);
            log.debug("缓存回源写入: key={}, ttl={}s", key, finalExpiration);
        } else {
            // 缓存穿透防护：缓存空值，短过期时间（5 分钟）
            redisTemplate.opsForValue().set(key, NULL_PLACEHOLDER, 300, TimeUnit.SECONDS);
            localCache.put(key, NULL_PLACEHOLDER);
            log.debug("缓存穿透防护: 缓存空值 key={}", key);
        }

        return result;
    }

    /**
     * 删除指定缓存（同时删除 Caffeine 和 Redis）
     *
     * @param key 缓存 Key
     */
    public void evict(String key) {
        localCache.invalidate(key);
        redisTemplate.delete(key);
        log.debug("缓存删除: key={}", key);
    }

    /**
     * 手动写入缓存（同时写入 Caffeine 和 Redis）
     *
     * @param key        缓存 Key
     * @param value      缓存值
     * @param expiration Redis 过期时间（秒）
     */
    public void put(String key, Object value, long expiration) {
        if (value == null) {
            redisTemplate.opsForValue().set(key, NULL_PLACEHOLDER, 300, TimeUnit.SECONDS);
            localCache.put(key, NULL_PLACEHOLDER);
        } else {
            long randomOffset = (long) (expiration * 0.2 * (Math.random() * 2 - 1));
            long finalExpiration = expiration + randomOffset;
            redisTemplate.opsForValue().set(key, value, finalExpiration, TimeUnit.SECONDS);
            localCache.put(key, value);
        }
    }

    /**
     * 更新缓存有效期（只更新redis不更新本地缓存）
     * @param key
     */
    public void updateExpire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 清空所有本地缓存
     */
    public void clearLocal() {
        localCache.invalidateAll();
        log.debug("本地缓存已全部清空");
    }
}

