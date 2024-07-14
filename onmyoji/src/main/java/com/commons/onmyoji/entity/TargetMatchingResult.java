package com.commons.onmyoji.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * 匹配结果
 * @author chishupeng
 * @date 2023/8/23 11:28 AM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TargetMatchingResult {

    /**
     * 窗口名称
     */
    private String windowName;

    /**
     * 匹配图片名称
     */
    private String targetImgName;

    /**
     * 已匹配次数
     */
    private Integer count = 0;

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


    public TargetMatchingResult(String windowName, String targetImgName,Integer x, Integer y, Integer width, Integer height) {
        this.windowName = windowName;
        this.locationX = x;
        this.locationY = y;
        this.imgWidth = width;
        this.imgHeight = height;
        this.targetImgName = targetImgName;
    }

    public TargetMatchingResult(String targetImgName) {
        this.targetImgName = targetImgName;
    }

    /**
     * 窗口名称相同即认为相同，坐标信息直接覆盖
     * @param obj 比较对象
     * @return true or false
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TargetMatchingResult)) {
            return false;
        }
        return Objects.equals(this.getTargetImgName(),((TargetMatchingResult) obj).getTargetImgName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetImgName);
    }
}
