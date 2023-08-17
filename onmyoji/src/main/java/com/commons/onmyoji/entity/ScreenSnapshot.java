package com.commons.onmyoji.entity;

import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * 屏幕快照
 * 饿汉式单例
 *
 * @author chishupeng
 * @date 2023/8/9 2:55 PM
 */
@Data
public class ScreenSnapshot {

    private static final ScreenSnapshot instance = new ScreenSnapshot();

    private BufferedImage bufferedImage;

    /**
     * 私有化构造函数，不允许外部通过构造函数实例化
     */
    private ScreenSnapshot() {
    }

    /**
     * 获取唯一可用的对象
     *
     * @return
     */
    public static ScreenSnapshot getInstance() {
        return instance;
    }

    /**
     * 获取唯一可用的对象
     *
     * @return
     */
    public static void clear() {
        instance.bufferedImage = null;
    }

}
