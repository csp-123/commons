package com.commons.onmyoji.job;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * Title: 挂机类型
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/20 17:12
 */
@Getter
@Setter
public class HangUpType {

    /**
     * 1. 限时    2. 限次    3. 不限（不关程序刷到断电）
     */
    private Integer type;

    /**
     * 次数
     */
    private Integer times;

    /**
     * 时长：默认分钟
     */
    private Integer time;

    /**
     * 单位
     */
    private TimeUnit unit;
}
