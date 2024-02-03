package com.commons.onmyoji.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Set<GameWindowSnapshotItem> snapshotItemList = new HashSet<>();

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


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (GameWindowSnapshotItem gameWindowSnapshotItem : snapshot.getSnapshotItemList()) {
            String windowName = gameWindowSnapshotItem.getWindowName();
            int width = gameWindowSnapshotItem.getWindowWidth();
            int height = gameWindowSnapshotItem.getWindowHeight();
            int x = gameWindowSnapshotItem.getX();
            int y = gameWindowSnapshotItem.getX();
            String info = String.format("[windowName:%s,width:%d,height:%d,x:%d,y:%d]", windowName,width,height,x,y);
            stringBuilder.append(info);
        }
        return stringBuilder.toString();
    }
}
