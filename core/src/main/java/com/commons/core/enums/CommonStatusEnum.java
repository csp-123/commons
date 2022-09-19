package com.commons.core.enums;

/**
 * @description: 通用状态码
 * @author: cccsp
 * @date: 2022/9/19 16:08
 */
public enum CommonStatusEnum {

    /**
     * 是
     */
    IS(1),

    /**
     * 否
     */
    NOT(0);

    /**
     * 状态码
     */
    private final Integer statusCode;

    CommonStatusEnum(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getStatus() {
        return this.statusCode;
    }
}
