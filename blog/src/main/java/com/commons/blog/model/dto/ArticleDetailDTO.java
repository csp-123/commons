package com.commons.blog.model.dto;

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
public class ArticleDetailDTO {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;

}
