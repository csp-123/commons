package com.commons.onmyoji.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenSnapshotItem {


    /**
     * 窗口名称
     */
    private String windowName;

    /**
     * 窗口程序x坐标
     */
    private int x;

    /**
     * 窗口程序y坐标
     */
    private int y;

    /**
     * 窗口宽
     */
    private int windowWidth;

    /**
     * 窗口高
     */
    private int windowHeight;

    /**
     * bufferedImage
     */
    private BufferedImage bufferedImage;

    /**
     * RGBData
     */
    private int[][] RGBData;

}