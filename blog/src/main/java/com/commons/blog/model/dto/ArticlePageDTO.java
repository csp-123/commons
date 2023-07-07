package com.commons.blog.model.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.commons.blog.model.entity.Article;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 文章分页入参
 *
 * @author chishupeng
 * @date 2023/7/5 7:33 PM
 */
@Data
public class ArticlePageDTO extends Page<Article> {

    /**
     * 标题
     */
    @Size(max = 50, message = "标题超长，最大长度为{max}")
    private String title;


    /**
     * 标签id todo
     */
    private Long tagId;


}
