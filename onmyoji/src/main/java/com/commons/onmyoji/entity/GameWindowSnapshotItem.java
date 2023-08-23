package com.commons.onmyoji.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * 游戏窗口快照
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GameWindowSnapshotItem {

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

    @Override
    public String toString() {
        return String.format("windowName:%s,x:%s,y:%s,width:%s,height:%s", windowName, x, y, windowWidth, windowHeight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameWindowSnapshotItem)) return false;
        GameWindowSnapshotItem that = (GameWindowSnapshotItem) o;
        return Objects.equals(windowName, that.windowName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(windowName);
    }
}