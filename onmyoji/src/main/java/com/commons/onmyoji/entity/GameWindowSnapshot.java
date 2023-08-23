package com.commons.onmyoji.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏窗口快照
 * 饿汉式单例
 *
 * @author chishupeng
 * @date 2023/8/9 2:55 PM
 */
@Setter
@Getter
public class GameWindowSnapshot {

    private static final GameWindowSnapshot snapshot = new GameWindowSnapshot();

    /**
     * 考虑多窗口情况，定义子类的集合
     */
    private List<GameWindowSnapshotItem> snapshotItemList = new ArrayList<>();

    /**
     * 私有化构造函数，不允许外部通过构造函数实例化
     */
    private GameWindowSnapshot() {
    }

    /**
     * 获取唯一可用的对象
     *
     */
    public static GameWindowSnapshot getInstance() {
        return snapshot;
    }

}
