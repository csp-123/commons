package com.commons.blog.generator;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisIdGenerator {
    // 开始的时间戳
    private static final long BEGIN_TIMESTAMP = 1640995200L;
    // 序列号位数
    private static final int OFFSET = 32;

    @Resource
    StringRedisTemplate stringRedisTemplate;


    /**
     * redis生成全局唯一id
     *
     * @param key 业务key  比如订单 就给他一个order
     */
    public Long redisIdGenerate(String busKey) {

        // 生成时间戳
        LocalDateTime date = LocalDateTime.now();
        long nowSecond = date.toEpochSecond(ZoneOffset.UTC);
        long timeStamp = nowSecond - BEGIN_TIMESTAMP;
        //    生成序列号
        // 生成日期，精确到天
        String formatDate =
                date.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        Long incrementId =
                stringRedisTemplate.opsForValue().increment(busKey + ":" + formatDate);

        // 位移加或运算，实现拼接
        return timeStamp << OFFSET | incrementId;
    }


    public static void main(String[] args) {
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        long second = dateTime.toEpochSecond(ZoneOffset.UTC);
        System.out.println(second);

    }
}