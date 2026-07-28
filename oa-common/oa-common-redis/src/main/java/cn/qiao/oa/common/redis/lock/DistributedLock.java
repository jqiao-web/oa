package cn.qiao.oa.common.redis.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁工具类（基于 Redisson）
 * 支持可重入、自动续期（看门狗）、公平锁
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DistributedLock {

    private final RedissonClient redissonClient;

    /**
     * 基础分布式锁（自动续期，看门狗默认 30 秒）
     */
    public <T> T executeWithLock(String lockKey, Supplier<T> supplier) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock(5, TimeUnit.SECONDS)) {
                try {
                    return supplier.get();
                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }
            throw new RuntimeException("获取分布式锁失败: " + lockKey);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("获取分布式锁被中断", e);
        }
    }

    /**
     * 带超时参数的分布式锁（不自动续期，到期强制释放）
     */
    public <T> T executeWithLock(String lockKey, long waitTime, long leaseTime,
                                  TimeUnit unit, Supplier<T> supplier) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock(waitTime, leaseTime, unit)) {
                try {
                    return supplier.get();
                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }
            throw new RuntimeException("获取分布式锁超时: " + lockKey);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("获取分布式锁被中断", e);
        }
    }

    /**
     * 公平锁（多个线程按请求顺序获取锁，适合排队场景）
     */
    public <T> T executeWithFairLock(String lockKey, Supplier<T> supplier) {
        RLock fairLock = redissonClient.getFairLock(lockKey);
        try {
            if (fairLock.tryLock(5, TimeUnit.SECONDS)) {
                try {
                    return supplier.get();
                } finally {
                    if (fairLock.isHeldByCurrentThread()) {
                        fairLock.unlock();
                    }
                }
            }
            throw new RuntimeException("获取公平锁失败: " + lockKey);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("获取公平锁被中断", e);
        }
    }
}
