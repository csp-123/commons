package com.commons.onmyoji.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 屏幕快照
 * 饿汉式单例
 *
 * @author chishupeng
 * @date 2023/8/9 2:55 PM
 */
@Accessors(chain = true)
@Setter
@Getter
public class ScreenSnapshot {

    private static final ScreenSnapshot snapshot = new ScreenSnapshot();

    /**
     * 窗口快照map  key-窗口名称 value-快照
     */
    private Map<String, ScreenSnapshotItem> snapshotItemMap = new HashMap<>();

    /**
     * 私有化构造函数，不允许外部通过构造函数实例化
     */
    private ScreenSnapshot() {
    }

    /**
     * 获取唯一可用的对象
     *
     */
    public static ScreenSnapshot getInstance() {
        return snapshot;
    }

}
