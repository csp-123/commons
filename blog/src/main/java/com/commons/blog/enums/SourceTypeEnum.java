package com.commons.blog.enums;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.sql.rowset.serial.SerialException;
import java.util.HashMap;

/**
 * 资源类型
 *
 * @author chishupeng
 * @date 2023/7/6 5:54 PM
 */
@Getter
public enum SourceTypeEnum {

    ARTICLE("文章", "ARTICLE"),

    TAG("标签", "TAG"),

    CLASSIFICATION("分类", "CLASSIFICATION");

    /**
     * 描述
     */
    private final String desc;

    /**
     * 状态码
     */
    private final String status;

    SourceTypeEnum(String desc, String status) {
        this.status = status;
        this.desc = desc;
    }

    /**
     * 寻值
     *
     * @return 枚举
     */
    public static SourceTypeEnum of(String status) {
        try {
            valueOf(status);
            return valueOf(status);
        } catch (Exception e) {
            throw new IllegalArgumentException("非法枚举");
        }
    }

}
