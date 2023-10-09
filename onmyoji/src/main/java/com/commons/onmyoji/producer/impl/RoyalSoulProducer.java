package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.components.Matcher;
import com.commons.onmyoji.config.RoyalSoulConfig;
import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.job.OnmyojiJob;
import com.commons.onmyoji.producer.InstanceZoneBaseProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Title: 御魂脚本处理器
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 22:39
 */
@Component("RoyalSoul")
@Slf4j
public class RoyalSoulProducer extends InstanceZoneBaseProducer<RoyalSoulConfig> {

    @Resource
    Matcher matcher;

    @Override
    public String getProducerName() {
        return "御魂";
    }

    @Override
    public void prepare(OnmyojiJob<RoyalSoulConfig> job) {
        // 配置： 层数、截图存放位置
        RoyalSoulConfig jobConfig = job.getConfig();
        String imgDirectory = System.getProperty("user.dir") + "\\" + jobConfig.imgPath + "\\";
        // 开始图片
        String start = imgDirectory + OnmyojiConstant.ROYAL_SOUL_START_BUTTON;
        // 奖励图片
        String reward = imgDirectory + OnmyojiConstant.ROYAL_SOUL_REWARD_BUTTON;
        // 结束图片
        String end = imgDirectory + OnmyojiConstant.ROYAL_SOUL_END_BUTTON;
        // start拼接上solo or team
        if (job.isSolo()) {
            String[] split = start.split("\\.");
            start = Arrays.stream(split)
                    .reduce((s1, s2) -> s1 + "_solo." + s2)
                    .orElse(start);
        } else {
            String[] split = start.split("\\.");
            start = Arrays.stream(split)
                    .reduce((s1, s2) -> s1 + "_team." + s2)
                    .orElse(start);
        }
        Set<String> targetImgList = new HashSet<>();
        targetImgList.add(start);
        targetImgList.add(end);
        targetImgList.add(reward);
        //图片匹配器初始化
        matcher.init(targetImgList);
    }

}
