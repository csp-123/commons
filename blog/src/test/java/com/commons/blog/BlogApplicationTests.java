package com.commons.blog;

import com.commons.blog.generator.RedisIdGenerator;
import com.commons.blog.mapper.ArticleMapper;
import com.commons.blog.model.entity.Article;
import com.commons.blog.service.ArticleService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class BlogApplicationTests {

    @Resource
    private ArticleService articleService;

    @Resource
    private ArticleMapper articleMapper;


    @Test
    void contextLoads() {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(100);
        List<Article> articles = articleMapper.listForTest(start, end);
    }

}
