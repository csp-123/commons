package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.components.Matcher;
import com.commons.onmyoji.entity.OnmyojiJob;
import com.commons.onmyoji.producer.InstanceZoneProducer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Title: 自定义场景处理器
 * Description:
 * Project: commons
 * Author: chish
 * Create Time:2023/5/21 15:08
 */
@Component("Customize")
public class CustomizeProducer implements InstanceZoneProducer {

    @Resource
    Matcher matcher;

    @Override
    public void produce(OnmyojiJob job) {

    }

    @Override
    public String getProducerName() {
        return "自定义场景";
    }

}