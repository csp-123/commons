package com.commons.onmyoji.job;

import com.commons.onmyoji.config.OnmyojiScriptConfig;
import com.commons.onmyoji.producer.InstanceZoneProducer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
     * 组队类型 1.单刷 2. 组队
     */
    private Integer teamType;

    /**
     * 队伍人数，默认为2
     */
    private Integer teamMembers = 2;

    /**
     * 挂机类型
     */
    private HangUpType hangUpType;


    /**
     * 挂机处理器
     */
    private InstanceZoneProducer producer;

    /**
     * 配置
     */
    private JOB_CONFIG config;

    public void start() {
//        OnmyojiDeamonTask thread = new OnmyojiDeamonTask();
//        thread.start();
        producer.produce(this);
//        thread.interrupt();

    }

}
