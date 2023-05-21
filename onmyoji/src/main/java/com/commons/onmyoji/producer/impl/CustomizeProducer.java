package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.config.CustomizeConfig;
import com.commons.onmyoji.config.RoyalSoulConfig;
import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.enums.HangUpTypeEnum;
import com.commons.onmyoji.enums.TeamTypeEnum;
import com.commons.onmyoji.job.OnmyojiJob;
import com.commons.onmyoji.matcher.Matcher;
import com.commons.onmyoji.producer.InstanceZoneBaseProducer;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Title: 自定义场景处理器
 * Description:
 * Project: commons
 * Author: chish
 * Create Time:2023/5/21 15:08
 */
@Component("Customize")
public class CustomizeProducer extends InstanceZoneBaseProducer<CustomizeConfig> {

    private static final Logger logger = LoggerFactory.getLogger(CustomizeProducer.class);

    ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    @Override
    public void produce(OnmyojiJob<CustomizeConfig> job) {
        // 脚本执行次数
        int count = 1;
        threadLocal.set(count);

        // 配置： 层数、截图存放位置
        CustomizeConfig jobConfig = job.getConfig();
        String imgDirectory = System.getProperty("user.dir") + "\\" + jobConfig.imgPath + "\\";


        // 开始图片
        String start = imgDirectory + OnmyojiConstant.CUSTOMIZE_START_BUTTON;
        // 奖励图片
        String reward = imgDirectory + OnmyojiConstant.CUSTOMIZE_REWARD_BUTTON;
        // 结束图片
        String end = imgDirectory + OnmyojiConstant.CUSTOMIZE_END_BUTTON;


        //图片匹配器
        Matcher matcher = new Matcher(width, height);

        executeOnce(start, end, reward, job, matcher);

        threadLocal.remove();
    }


    @SneakyThrows
    private void executeOnce(String start, String end, String reward, OnmyojiJob<CustomizeConfig> job, Matcher matcher) {

        // 开始
        boolean startSuccess = false;
        while (!startSuccess) {
            startSuccess = matcher.clickBlocking(start, true, 1, false);
        }

        // 领取奖励
        boolean rewardSuccess= false;
        while (!rewardSuccess) {
            rewardSuccess = matcher.clickBlocking(reward, true, 1, false);
        }

        // 结束
        boolean endSuccess = false;
        while (!endSuccess) {
            endSuccess = matcher.clickBlocking(end, true, 1, false);
        }

    }
    @Override
    public String getProcuderName() {
        return "自定义场景";
    }
}