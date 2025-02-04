package com.commons.onmyoji.entity;

import lombok.Data;
import org.apache.spark.sql.sources.In;

import java.awt.image.BufferedImage;

/**
 * Title: 图片
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2024/11/13 20:37
 */
@Data
public class Picture {

    /**
     * 序号
     */
    private Integer order;

    /**
     * 所在目录
     */
    private String imgParentPath;

    /**
     * 是否需要定位（必须点击指定位置）
     */
    private Boolean locate;

    /**
     * 图片名称
     */
    private String imgName;

    /**
     * 图片路径
     */
    private String imgPath;

    /**
     * rgb数据
     */
    private int[][] rgb;

    /**
     * bufferedImage
     */
    private BufferedImage bf;
}
