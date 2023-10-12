package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.components.Matcher;
import com.commons.onmyoji.config.DailyJobConfig;
import com.commons.onmyoji.config.OnmyojiScriptConfig;
import com.commons.onmyoji.job.OnmyojiJob;
import com.commons.onmyoji.producer.CustomizeProcessBaseProducer;
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
public class ChaoGuiWangProducer extends CustomizeProcessBaseProducer<OnmyojiScriptConfig> {

    @Resource
    Matcher matcher;

    @Override
    public String getProducerName() {
        return "超鬼王";
    }


    /**
     * 1. 探索、觉醒、御魂、御灵等
     * 2. 使用鬼王票 -> 清鬼王
     * 3. 以上流程循环
     * @param job
     */
    @Override
    public void process(OnmyojiJob<OnmyojiScriptConfig> job) {
        // 配置： 层数、截图存放位置
        OnmyojiScriptConfig jobConfig = job.getConfig();
        String imgDirectory = System.getProperty("user.dir") + "\\" + jobConfig.imgPath + "\\";

    }

}
