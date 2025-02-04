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
     *  多开
     */
    MULTI_OPEN("MULTI_OPEN", "多开"),

    /**
     * 组队多开，组队多开指所有窗口是在一个队伍，一个窗口为队长，其余为队员。仅支持双开！！
     */
    TEAM_MULTI_OPEN("TEAM_MULTI_OPEN", "组队多开"),

    /**
     * 组队匹配，组队匹配指与其他人组队刷副本，不限制在同一队伍，但限制各窗口账号必须为队员！
     */
    TEAM_MATCHING("TEAM_MATCHING", "组队匹配");

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
