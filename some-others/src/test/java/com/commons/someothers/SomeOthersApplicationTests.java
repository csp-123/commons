package com.commons.someothers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SomeOthersApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        String a = "csp";
        Map<String,  String> b = new HashMap<>();
        b.put("a", "csp");
        redisTemplate.opsForValue().set(a, b);
        Object csp = redisTemplate.opsForValue().get("csp");
        System.out.println(csp);
    }

}
