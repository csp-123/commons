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

    /**
     * key:windowName，窗口名称
     * value: Set<TargetMatchingResult> 窗口内的所有图片匹配结果
     */
    private Map<String, Set<TargetMatchingResult>> resultItemMap = new HashMap<>();


    /**
     * 私有化构造函数，不允许外部通过构造函数实例化
     */
    public MatchResult() {
    }

}
