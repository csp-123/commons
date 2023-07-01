package com.commons.core.util;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Description:
 * 分布式锁
 * Project: commons
 * Author: chish
 * Create Time:2023/7/1 1:01
 */
@Slf4j
@Component
public class RedisLock implements ApplicationContextAware {

    @Setter
    private static RedissonClient redissonClient;

    /**
     * 分布式锁
     * @param key key
     * @param waitTime 等待时间
     * @param lockTime 持锁时间
     * @param consumer 消费逻辑
     * @param timeUnit 时间单位
     * @param <T> 参数类型
     */
    public static <T> void lock(String key, Long waitTime, Long lockTime, Consumer<T> consumer, T param, TimeUnit timeUnit) {
        boolean tryLock = false;
        RLock lock = redissonClient.getLock(key);
        try {
            waitTime = waitTime == null ? 0L : waitTime;
            if (Objects.isNull(lockTime)) {
                tryLock = lock.tryLock(waitTime, timeUnit);
            } else {
                tryLock = lock.tryLock(waitTime, lockTime, timeUnit);
            }
            if (!tryLock) {
                log.error("加锁失败");
            }
            consumer.accept(param);
        } catch (Exception e) {
            log.error("加锁异常，{}", e.getMessage());
        } finally {
            if (tryLock && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }


    /**
     * 分布式锁
     * @param key key
     * @param waitTime 等待时间
     * @param lockTime 持锁时间
     * @param function 功能函数
     * @param param 参数
     * @param timeUnit 时间单位
     * @return R
     * @param <T> 入参类型
     * @param <R> 出餐类型
     */
    public static <T, R> R  lock(String key, Long waitTime, Long lockTime, Function<T, R> function, T param, TimeUnit timeUnit) {
        boolean tryLock = false;
        RLock lock = redissonClient.getLock(key);
        try {
            waitTime = waitTime == null ? 0L : waitTime;
            if (Objects.isNull(lockTime)) {
                tryLock = lock.tryLock(waitTime, timeUnit);
            } else {
                tryLock = lock.tryLock(waitTime, lockTime, timeUnit);
            }
            if (!tryLock) {
                log.error("加锁失败");
            }
            return function.apply(param);
        } catch (Exception e) {
            log.error("加锁异常，{}", e.getMessage());
            throw new RuntimeException();
        } finally {
            if (tryLock && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        setRedissonClient(applicationContext.getBean(RedissonClient.class));
    }
}