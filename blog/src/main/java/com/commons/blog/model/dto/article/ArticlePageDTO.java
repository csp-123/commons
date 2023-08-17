package com.commons.blog.model.dto.article;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.commons.blog.model.dto.BasePageDTO;
import com.commons.blog.model.dto.OrderCondition;
import com.commons.blog.model.entity.Article;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 文章分页入参
 *
 * @author chishupeng
 * @date 2023/7/5 7:33 PM
 */
@Data
@ApiModel("分页")
public class ArticlePageDTO extends BasePageDTO {

    /**
     * 标题
     */
    @Size(max = 50, message = "标题超长，最大长度为{max}")
    @ApiModelProperty("标题")
    private String title;


    /**
     * 标签id todo
     */
    @ApiModelProperty("标签id")
    private Long tagId;

    /**
     * 分类id todo
     */
    @ApiModelProperty("分类id")
    private Long classificationId;




}
