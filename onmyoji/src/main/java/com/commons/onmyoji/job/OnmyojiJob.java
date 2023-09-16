package com.commons.onmyoji.job;

import com.commons.onmyoji.config.OnmyojiScriptConfig;
import com.commons.onmyoji.producer.InstanceZoneProducer;
import com.commons.onmyoji.components.GameWindowFreshTask;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Timer;

/**
 * Title:
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/20 16:32
 */
@Getter
@Setter
@EqualsAndHashCode
public class OnmyojiJob <JOB_CONFIG extends OnmyojiScriptConfig> {

    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否单刷
     */
    private boolean solo;

    /**
     * 挂机类型
     */
    private HangUpType hangUpType;


    /**
     * 副本挂机处理器
     */
    private InstanceZoneProducer producer;

    /**
     * 配置
     */
    private JOB_CONFIG config;

    /**
     * 启动
     */
    public void start() {
        producer.produce(this);
    }

}
