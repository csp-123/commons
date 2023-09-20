package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.components.Matcher0920;
import com.commons.onmyoji.config.CustomizeConfig;
import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.job.OnmyojiJob;
import com.commons.onmyoji.producer.InstanceZoneBaseProducer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * Title: 自定义场景处理器
 * Description:
 * Project: commons
 * Author: chish
 * Create Time:2023/5/21 15:08
 */
@Component("Customize")
public class CustomizeProducer extends InstanceZoneBaseProducer<CustomizeConfig> {

    @Resource
    Matcher0920 matcher0920;

    @Override
    public String getProducerName() {
        return "自定义场景";
    }

    @Override
    public void prepare(OnmyojiJob<CustomizeConfig> job) {
        // 配置： 层数、截图存放位置
        CustomizeConfig jobConfig = job.getConfig();
        String imgDirectory = System.getProperty("user.dir") + "\\" + jobConfig.getImgPath() + "\\";
        // 开始图片
        String start = imgDirectory + OnmyojiConstant.CUSTOMIZE_START_BUTTON;
        // 奖励图片
        String reward = imgDirectory + OnmyojiConstant.CUSTOMIZE_REWARD_BUTTON;
        // 结束图片
        String end = imgDirectory + OnmyojiConstant.CUSTOMIZE_END_BUTTON;
        Set<String> imgList = new HashSet<>();
        imgList.add(start);
        imgList.add(reward);
        imgList.add(end);
        matcher0920.init(imgList);

    }
}