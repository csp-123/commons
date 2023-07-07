package com.commons.core.enums;

import lombok.Getter;

/**
 * Description:
 * 通用状态码
 * Project: commons
 * Author: cccsp
 * Create Time:2022/9/19 16:08
 */
@Getter
public enum CommonStatusEnum {

    /**
     * 是
     */
    TRUE((byte) 1, "是"),

    /**
     * 否
     */
    FALSE((byte) 0, "否");

    /**
     * 状态码
     */
    private final byte statusCode;

    /**
     * 描述
     */
    private final String desc;

    CommonStatusEnum(byte statusCode, String desc) {
        this.statusCode = statusCode;
        this.desc = desc;
    }

    public static byte getTrue() {
        return TRUE.statusCode;
    }

    public static byte getFalse() {
        return FALSE.statusCode;
    }
}
