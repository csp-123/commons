package com.commons.blog.model.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

/**
 * 文章
 *
 * @author chishupeng
 * @date 2023/7/5 7:11 PM
 */
@Data
@ApiModel("详情")
public class ArticleDetailDTO {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id", required = true)
    private Long id;

}
