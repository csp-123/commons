package com.commons.onmyoji.enums;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TeamTypeEnum {

    /**
     * 单刷
     */
    SOLO("SOLO", "单刷"),

    /**
     * 组队 - 多开
     */
    TEAM_MULTI_OPEN("TEAM_MULTI_OPEN", "组队-多开"),

    /**
     * 组队 - 匹配
     */
    TEAM_MATCHING("TEAM_MATCHING", "组队-匹配");

    /**
     * 类型
     */
    private final String type;

    /**
     * 描述
     */
    private final String desc;

    TeamTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private static final Map<String, TeamTypeEnum> lookUpFrom = new HashMap<>();

    static {
        for (TeamTypeEnum value : TeamTypeEnum.values()) {
            lookUpFrom.put(value.getType(), value);
        }
    }

    public static TeamTypeEnum findByType(String type) {
        return lookUpFrom.get(type);
    }
}
