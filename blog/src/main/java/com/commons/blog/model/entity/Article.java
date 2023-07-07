package com.commons.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文章
 * </p>
 *
 * @author baomidou
 * @since 2023-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("blog_article")
public class Article extends BaseSourceEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 链接
     */
    @TableField("url")
    private String url;

    /**
     * 内容
     */
    @TableField("content")
    private String content;
}
