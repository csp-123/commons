package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.components.Matcher;
import com.commons.onmyoji.config.DailyJobConfig;
import com.commons.onmyoji.config.RoyalSoulConfig;
import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.job.OnmyojiJob;
import com.commons.onmyoji.producer.CustomizeProcessBaseProducer;
import com.commons.onmyoji.producer.InstanceZoneBaseProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Title: 每日任务
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 22:39
 */
@Component("DailyJob")
@Slf4j
public class DailyJobProducer extends CustomizeProcessBaseProducer<DailyJobConfig> {

    @Resource
    Matcher matcher;

    @Override
    public String getProducerName() {
        return "每日任务";
    }

    /**
     * 1. 地域鬼王
     * 2. 御魂*n
     * 3. 觉醒*n
     * 4. 探索*n
     * 5. 结界突破*n
     * @param job
     */
    @Override
    public void process(OnmyojiJob<DailyJobConfig> job) {
        // 配置： 层数、截图存放位置
        DailyJobConfig jobConfig = job.getConfig();
        String imgDirectory = System.getProperty("user.dir") + "\\" + jobConfig.imgPath + "\\";

    }

}
