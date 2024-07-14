package com.commons.onmyoji.producer;


import com.commons.onmyoji.entity.OnmyojiJob;

/**
 * Title: 副本处理器
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 22:34
 */
public interface InstanceZoneProducer {

    void produce(OnmyojiJob job);

    String getProducerName();


}
