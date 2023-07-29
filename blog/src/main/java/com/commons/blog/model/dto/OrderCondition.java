package com.commons.blog.model.dto;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * 排序器
 *
 * @author chishupeng
 * @date 2023/7/29 10:24 PM
 */
@ApiModel("排序器")
@Getter
public class OrderCondition extends OrderItem {

    /**
     * 排序器优先级：越大优先级越高
     */
    @ApiModelProperty("排序器优先级")
    private Integer priority;
}
