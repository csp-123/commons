package com.commons.onmyoji.entity;

import lombok.Data;

/**
 * 图片位置
 */
@Data
public class PicLocation {
    private int left;
    private int right;
    private int top;
    private int bottom;

    public PicLocation(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

}
