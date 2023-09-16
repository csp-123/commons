package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.config.RoyalSoulConfig;
import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.enums.HangUpTypeEnum;
import com.commons.onmyoji.enums.TeamTypeEnum;
import com.commons.onmyoji.job.HangUpType;
import com.commons.onmyoji.job.OnmyojiJob;
import com.commons.onmyoji.components.Matcher;
import com.commons.onmyoji.producer.InstanceZoneBaseProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;

/**
 * Title: 御魂脚本处理器
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 22:39
 */
@Component("RoyalSoul")
public class RoyalSoulProducer extends InstanceZoneBaseProducer<RoyalSoulConfig> {

    private static final Logger logger = LoggerFactory.getLogger(RoyalSoulProducer.class);

    @Resource
    Matcher matcher;

    /**
     * 从哪个界面开始，庭院？还是御魂界面？ 或者兼容？
     * 1. 单刷 or 组队？
     * 2. 限时 or 限次 or 刷到死？
     * 容错机制：
     * 1. 宠物发现额外奖励
     * 2. 组队时队友超时，重新邀请队友
     *
     * @param job
     */
    @Override
    public void produce(OnmyojiJob<RoyalSoulConfig> job) {

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

        List<String> targetImgList = new ArrayList<>();
        targetImgList.add(start);
        targetImgList.add(end);
        targetImgList.add(reward);
        //图片匹配器初始化
        matcher.init(targetImgList);

        // 限次 todo
        HangUpType hangUpType = job.getHangUpType();
        if (hangUpType.getType().equals(HangUpTypeEnum.TIMES.getCode())) {
        }
        //  限时
        if (hangUpType.getType().equals(HangUpTypeEnum.TIME.getCode())) {
            long endTime =System.currentTimeMillis() + hangUpType.getTime() * 60 * 1000L;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Boolean success = matcher.matchAllImg(job.isSolo());
                    logger.info("匹配结果：{}", success);
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask, new Date(), 500);
            // 未到结束时间时空转
            while (System.currentTimeMillis() <= endTime) {}
            timer.cancel();
        }
        // 不限 todo
        if (hangUpType.getType().equals(HangUpTypeEnum.FOREVER.getCode())) {
        }
    }




    @Override
    public String getProducerName() {
        return "御魂";
    }
}
