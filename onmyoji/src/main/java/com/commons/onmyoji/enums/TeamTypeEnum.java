package com.commons.onmyoji.enums;

/**
 * @description: 组队类型枚举
 * @author: chish
 * @date: 2023/3/11 23:56
 */
public enum TeamTypeEnum {
    /**
     * 单刷
     */
    SOLO(1, "单刷"),

    /**
     * 组队
     */
    TEAM(2, "组队");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String desc;

    TeamTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
