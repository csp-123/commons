package com.commons.blog.enums;

import com.mysql.cj.Query;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 资源状态枚举
 *
 * @author chishupeng
 * @date 2023/7/4 12:48 AM
 */
@Getter
@Accessors(chain = true)
public enum SourceStatusEnum {

    /**
     * 初始态
     */
    INITIAL("INITIAL", "初始态");

    /**
     * 状态
     */
    private String status;

    /**
     * 描述
     */
    private String desc;

    SourceStatusEnum(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }

}
