package com.commons.core.enums;

import lombok.Getter;

/**
 * Description:
 *      通用状态码
 * Project: commons
 * Author: cccsp
 * Create Time:2022/9/19 16:08
 */
@Getter
public enum CommonStatusEnum {

    /**
     * 是
     */
    TRUE(1, "是"),

    /**
     * 否
     */
    FALSE(0, "否");

    /**
     * 状态码
     */
    private final Integer statusCode;

    /**
     * 描述
     */
    private final String desc;

    CommonStatusEnum(Integer statusCode, String desc) {
        this.statusCode = statusCode;
        this.desc = desc;
    }

    public static Integer getTrue() {
        return TRUE.statusCode;
    }

    public static Integer getFalse() {
        return FALSE.statusCode;
    }
}
