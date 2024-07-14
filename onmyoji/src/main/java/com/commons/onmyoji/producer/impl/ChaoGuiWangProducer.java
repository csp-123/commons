package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.components.Matcher;
import com.commons.onmyoji.entity.OnmyojiJob;
import com.commons.onmyoji.producer.InstanceZoneProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Title: 每日任务
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 22:39
 */
@Component("ChaoGuiWang")
@Slf4j
public class ChaoGuiWangProducer implements InstanceZoneProducer {

    @Resource
    Matcher matcher;

    @Override
    public void produce(OnmyojiJob job) {

    }

    @Override
    public String getProducerName() {
        return "超鬼王";
    }




}
