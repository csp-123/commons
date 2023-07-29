package com.commons.someothers;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SpringBootTest
class SomeOthersApplicationTests {

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    RedissonClient redissonClient;

    @Test
    void contextLoads() {
        String a = "csp";
        Map<String, String> b = new HashMap<>();
        b.put("a", "csp");
        redisTemplate.opsForValue().set(a, b);
        Object csp = redisTemplate.opsForValue().get("csp");
        System.out.println(csp);

    }


    @Test
    void test1() throws IOException {

        String script = IOUtils.toString(new FileInputStream("D:\\Practice\\commons\\auth\\src\\main\\resources\\lua\\test1.lua"), StandardCharsets.UTF_8);
        System.out.println(script);

        Object eval = redissonClient.getScript().eval(RScript.Mode.READ_WRITE, script, RScript.ReturnType.STATUS, Collections.singletonList("username"));
        System.out.println(eval.toString());


        List<Object> list = new ArrayList<>();
        list.add(null);

        List<String> list1 = new LinkedList<>();
        list1.add("a");

    }



}
