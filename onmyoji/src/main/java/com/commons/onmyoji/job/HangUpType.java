package com.commons.onmyoji.job;

/**
 * Title: 挂机类型
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/20 17:12
 */
public class HangUpType {

    /**
     * 1. 限时 2. 限次 3. 不限（不关程序刷到断电）
     */
    private Integer type;

    /**
     * 次数
     */
    private Integer times;

    /**
     * 时长：分钟
     */
    private Integer time;
}
