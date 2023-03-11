package com.commons.onmyoji.enums;

/**
 * @description: 挂机类型枚举
 * @author: chish
 * @date: 2023/3/11 23:56
 */
public enum HangUpTypeEnum {
    /**
     * 按时间
     */
    TIME(1, "按时长"),

    /**
     * 按次
     */
    TIMES(2, "按次数"),

    /**
     * 刷到死
     */
    FOREVER(3, "一直刷");
    private Integer code;

    private String desc;

    HangUpTypeEnum(Integer code, String desc) {
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
