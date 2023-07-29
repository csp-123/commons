package com.commons.blog.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页基类
 *
 * @author chishupeng
 * @date 2023/7/7 6:34 PM
 */
@Data
@ApiModel("分页")
public class BasePageDTO {

    /**
     * 当前页
     */
    @ApiModelProperty("当前页")
    private Integer pageNo = 1;

    /**
     * 分页大小
     */
    @ApiModelProperty("分页大小")
    private Integer pageSize = 20;

}
