package com.commons.blog.model.vo.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.commons.blog.model.entity.Article;
import com.commons.blog.model.vo.BaseSourceVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.function.Function;

/**
 * 文章列表VO
 *
 * @author chishupeng
 * @date 2023/7/5 7:32 PM
 */
@Data
public class ArticlePageVO extends BaseSourceVO {

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 链接
     */
    @ApiModelProperty("链接")
    private String url;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;
}
