package com.commons.blog.model.vo;

import com.commons.blog.model.enums.SourceTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * VO基类
 *
 * @author chishupeng
 * @date 2023/7/7 5:52 PM
 */
@Data
public class BaseSourceVO {

    /**
     * 资源唯一标识
     */
    @ApiModelProperty("资源id")
    private Long sourceId;

    /**
     * 资源类型
     * @see SourceTypeEnum
     */
    @ApiModelProperty("资源类型")
    private String sourceType;

    /**
     * 资源状态
     * @see SourceTypeEnum
     */
    @ApiModelProperty("资源状态")
    private String sourceStatus;
}
