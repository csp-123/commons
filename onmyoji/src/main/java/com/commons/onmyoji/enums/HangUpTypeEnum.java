package com.commons.onmyoji.enums;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum HangUpTypeEnum {

    TIME_FROM_NOW("TIME_FROM_NOW", "时长"),

    TIMES("TIMES", "次数"),

    ;
    private String type;

    private String desc;

    HangUpTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private static final Map<String, HangUpTypeEnum> lookUpFrom = new HashMap<>();

    static {
        for (HangUpTypeEnum value : HangUpTypeEnum.values()) {
            lookUpFrom.put(value.getType(), value);
        }
    }

    public static HangUpTypeEnum findByType(String type) {
        return lookUpFrom.get(type);
    }

}
