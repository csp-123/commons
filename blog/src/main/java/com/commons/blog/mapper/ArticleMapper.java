package com.commons.blog.mapper;

import com.commons.blog.model.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 文章 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2023-07-04
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    public List<Article> listForTest(LocalDate start, LocalDate end);

}
