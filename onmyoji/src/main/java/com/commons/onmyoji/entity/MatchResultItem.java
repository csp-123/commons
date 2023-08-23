package com.commons.onmyoji.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author chishupeng
 * @date 2023/8/23 11:28 AM
 */
@Getter
@Setter
public class MatchResultItem {

    /**
     * 窗口名称
     */
    private String windowName;

    /**
     * x坐标
     */
    private Integer locationX;

    /**
     * Y坐标
     */
    private Integer locationY;

    /**
     * 宽
     */
    private Integer imgWidth;

    /**
     * 高
     */
    private Integer imgHeight;


    public MatchResultItem(String windowName, Integer x, Integer y, Integer width, Integer height) {
        this.windowName = windowName;
        this.locationX = x;
        this.locationY = y;
        this.imgWidth = width;
        this.imgHeight = height;

    }

    /**
     * 窗口名称相同即认为相同，坐标信息直接覆盖
     * @param obj 比较对象
     * @return true or false
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MatchResultItem)) {
            return false;
        }
        return Objects.equals(this.getWindowName(),((MatchResultItem) obj).getWindowName());
    }
}
