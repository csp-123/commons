package com.commons.core.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页参数基类
 *
 * @author chishupeng
 * @date 2023/7/6 4:29 PM
 */
@Data
public class BasePage {

    /**
     * 当前页
     */
    @ApiModelProperty("当前页")
    private Integer pageNo;

    /**
     * 分页大小
     */
    @ApiModelProperty("分页大小")
    private Integer pageSize;
}