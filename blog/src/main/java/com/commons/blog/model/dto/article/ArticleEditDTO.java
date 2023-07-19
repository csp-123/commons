package com.commons.blog.model.dto.article;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.checkerframework.checker.units.qual.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 文章
 *
 * @author chishupeng
 * @date 2023/7/5 7:11 PM
 */
@Data
@ApiModel("新增、编辑参数")
public class ArticleEditDTO {

    /**
     * id 更新时id必填
     */
    @ApiModelProperty("id")
    private Long id;


    /**
     * 资源类型
     */
    @NotBlank(message = "资源类型不能为空")
    @ApiModelProperty(value = "资源类型", required = true)
    private String sourceType;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @Size(max = 50, message = "标题超长，最大长度为{max}")
    @ApiModelProperty(value = "标题", required = true)
    private String title;

    /**
     * 链接
     */
    @NotBlank
    @Size(max = 1000, message = "url超长，最大长度为{max}")
    @ApiModelProperty(value = "链接", required = true)
    private String url;

    /**
     * 内容
     */
    @NotBlank
    @ApiModelProperty(value = "内容", required = true)
    private String content;


}
