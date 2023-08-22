package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.config.RoyalSoulConfig;
import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.enums.HangUpTypeEnum;
import com.commons.onmyoji.enums.TeamTypeEnum;
import com.commons.onmyoji.job.OnmyojiJob;
import com.commons.onmyoji.matcher.ImgMatcher;
import com.commons.onmyoji.matcher.Matcher;
import com.commons.onmyoji.matcher.SimilarMatcher2;
import com.commons.onmyoji.producer.InstanceZoneBaseProducer;
import com.commons.onmyoji.service.CommonService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

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
        if (job.getTeamType().equals(TeamTypeEnum.SOLO.getCode())) {
            String[] split = start.split("\\.");
            start = Arrays.stream(split)
                    .reduce((s1, s2) -> s1 + "_solo." + s2)
                    .orElse(start);
        }
        if (job.getTeamType().equals(TeamTypeEnum.TEAM.getCode())) {
            String[] split = start.split("\\.");
            start = Arrays.stream(split)
                    .reduce((s1, s2) -> s1 + "_team." + s2)
                    .orElse(start);
        }

        List<String> targetImgList = new ArrayList<>();
        targetImgList.add(start);
        targetImgList.add(end);
        targetImgList.add(reward);
        //图片匹配器
        SimilarMatcher2 matcher = new SimilarMatcher2(targetImgList);


        // 处理挂机时长
        if (job.getHangUpType().getType().equals(HangUpTypeEnum.TIMES.getCode())) {
            // 限次 todo
        } else if (job.getHangUpType().getType().equals(HangUpTypeEnum.TIME.getCode())) {
            //  限时
            Integer minute = job.getHangUpType().getTime();
            long endTime = System.currentTimeMillis() + 60L * 1000 * minute;
            boolean solo = job.getTeamType().equals(TeamTypeEnum.SOLO.getCode());
            matcher.matchAll(solo, endTime);
//            matcher.clickAll(solo, endTime);
        } else if (job.getHangUpType().getType().equals(HangUpTypeEnum.FOREVER.getCode())) {
            // 不限 todo
        }
    }




    @Override
    public String getProcuderName() {
        return "御魂";
    }
}
