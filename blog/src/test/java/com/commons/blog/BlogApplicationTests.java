package com.commons.blog;

import com.commons.blog.generator.RedisIdGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class BlogApplicationTests {

    @Resource
    private RedisIdGenerator redisIdGenerator;

    @Test
    void contextLoads() {
        System.out.println(redisIdGenerator.redisIdGenerate("aaa"));
    }

}
