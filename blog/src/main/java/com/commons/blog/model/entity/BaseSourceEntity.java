package com.commons.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.commons.core.pojo.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 抽象资源基类
 *
 * @author chishupeng
 * @date 2023/7/3 10:36 PM
 */
@Data
@Accessors(chain = true)
public class BaseSourceEntity extends BaseEntity<Long> {

    /**
     * 资源唯一标识
     */
    @TableField(value = "source_id")
    private Long sourceId;

    /**
     * 资源类型
     * todo 枚举
     */
    @TableField(value = "source_type")
    private String sourceType;

    /**
     * 资源状态
     * todo 枚举
     */
    @TableField(value = "source_status")
    private String sourceStatus;



}
