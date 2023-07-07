package com.commons.onmyoji.enums;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @description: 组队类型枚举
 * @author: chish
 * @date: 2023/3/11 23:56
 */
public enum TeamTypeEnum {

    /**
     * 单刷
     */
    ONLY_ONE(1, "单刷"),

    /**
     * 多开
     */
    MORE(2, "多开");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String desc;

    private static final Map<Integer, TeamTypeEnum> lookUp = Maps.newHashMap();

    static {
        for (TeamTypeEnum value : TeamTypeEnum.values()) {
            lookUp.put(value.code, value);
        }
    }

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


    public static TeamTypeEnum find(Integer code) {
        return lookUp.get(code);
    }
}
