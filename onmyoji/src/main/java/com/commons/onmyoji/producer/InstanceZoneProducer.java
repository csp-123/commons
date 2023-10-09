package com.commons.onmyoji.producer;

import com.commons.onmyoji.config.OnmyojiScriptConfig;
import com.commons.onmyoji.job.OnmyojiJob;

/**
 * Title: 副本处理器
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 22:34
 */
public interface InstanceZoneProducer<CONFIG extends OnmyojiScriptConfig> {

    void produce(OnmyojiJob<CONFIG> job);

    String getProducerName();

    void stop();
}
