package com.commons.blog.model.vo.article;

import com.commons.blog.model.vo.BaseSourceVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文章详情VO
 *
 * @author chishupeng
 * @date 2023/7/5 7:32 PM
 */
@Data
public class ArticleDetailVO extends BaseSourceVO {

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
