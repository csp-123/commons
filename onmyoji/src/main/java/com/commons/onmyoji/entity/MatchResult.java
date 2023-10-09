package com.commons.onmyoji.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * 匹配结果
 *
 * @author chishupeng
 * @date 2023/8/23 11:22 AM
 */
@Setter
@Getter
public class MatchResult {

//    private static final MatchResult result = new MatchResult();

    /**
     * key:imgPath，即图片
     * value: Set<MatchResultItem> 所有游戏窗口的匹配结果，默认每个游戏窗口只会有一个匹配点位
     */
    private Map<String, Set<MatchResultItem>> resultItemMap = new HashMap<>();

    /**
     * 点击计数器
     */
    private Map<String, Integer> clickCountMap = new HashMap<>();

    /**
     * 私有化构造函数，不允许外部通过构造函数实例化
     */
    public MatchResult() {
    }

//    /**
//     * 获取唯一可用的对象
//     *
//     */
//    public static MatchResult getInstance() {
//        return result;
//    }
}
