package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.config.RoyalSoulConfig;
import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.enums.HangUpTypeEnum;
import com.commons.onmyoji.enums.TeamTypeEnum;
import com.commons.onmyoji.job.OnmyojiJob;
import com.commons.onmyoji.matcher.ImgMatcher;
import com.commons.onmyoji.matcher.Matcher;
import com.commons.onmyoji.producer.InstanceZoneBaseProducer;
import com.commons.onmyoji.service.CommonService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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

    ThreadLocal<Integer> threadLocal = new ThreadLocal<>();


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
        // 脚本执行次数
        int count = 0;
        threadLocal.set(count);

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

        //图片匹配器
        Matcher matcher = new Matcher(width, height);


        // 处理挂机时长
        if (job.getHangUpType().getType().equals(HangUpTypeEnum.TIMES.getCode())) {
            // 限次
            for (int i = 1; i <= job.getHangUpType().getTimes(); i++) {
                executeOnce(start, end, reward, job, matcher);
            }
        } else if (job.getHangUpType().getType().equals(HangUpTypeEnum.TIME.getCode())) {
            // 限时
            long endTime = System.currentTimeMillis() + 60 * 1000 * 1000;
            while (System.currentTimeMillis() <= endTime) {
                executeOnce(start, end, reward, job, matcher);
            }
        } else if (job.getHangUpType().getType().equals(HangUpTypeEnum.FOREVER.getCode())) {
            // 不限
            while (true) {
                executeOnce(start, end, reward, job, matcher);
            }
        }

        threadLocal.remove();
    }

    /**
     * 执行脚本
     *
     * @param start 开始图片
     * @param end 结束图片
     * @param job 任务
     */
    private void executeOnce(String start, String end, String reward, OnmyojiJob<RoyalSoulConfig> job, Matcher matcher) {
        //  计数器
        Integer count = threadLocal.get();
        if (count == null) {
            count = 1;
        }

        logger.info(String.format("=============执行第%s次挂机脚本，处理器：[%s]，组队类型：[%s]=============", count, getProcuderName(), TeamTypeEnum.find(job.getTeamType()).getDesc()));
        if (job.getTeamType().equals(TeamTypeEnum.SOLO.getCode())) {
            executeOnceInSoloMod(start, end, reward, job, matcher);
        }
        if (job.getTeamType().equals(TeamTypeEnum.TEAM.getCode())) {
            executeOnceInTeamMod(start, end, reward, job, matcher);
        }
        if (job.getTeamType().equals(TeamTypeEnum.TEAM_ONLINE.getCode())) {
            executeOnceInTeamOnlineMod(start, end, reward, job, matcher);
        }
        logger.info("=============执行结束=============");
        threadLocal.set(++count);
    }



    /**
     * 执行一次挂机脚本 - 单刷模式
     */
    @SneakyThrows
    private void executeOnceInSoloMod(String start, String end, String reward, OnmyojiJob<RoyalSoulConfig> job, Matcher matcher) {
        // todo 尚未改造，单刷较少

        // 开始
        boolean success;
        do {
            success = matcher.clickBlocking(start, true, 1, false);
        } while (success);

        // 睡眠10s，不会有人能15s刷魂吧
        Thread.sleep(15000);

        // 领取奖励
        do {
            success = matcher.clickBlocking(reward, true, 1, false);
        } while (success);

        // 结束
        do {
            success = matcher.clickBlocking(end, true, 1, false);
        } while (success);
    }

    /**
     * 执行一次挂机脚本 - 组队模式
     * 组队模式下，需要对屏幕内的所有命中位置进行点击，适配多开的情况
     *
     * @param start 开始图片路径
     * @param end   结束图片路径
     * @param job   job配置
     */
    @SneakyThrows
    private void executeOnceInTeamMod(String start, String end, String reward, OnmyojiJob<RoyalSoulConfig> job, Matcher matcher) {

        // 开始
        boolean success;
        do {
            success = matcher.clickBlocking(start, true, 1, false);
        } while (success);

        // 睡眠10s，不会有人能15s刷魂吧
        Thread.sleep(15000);

        // 领取奖励
        do {
            success = matcher.clickBlocking(reward, true, 2, false);
        } while (success);

        // 结束
        do {
            success = matcher.clickBlocking(end, true, 2, false);
        } while (success);

    }


    /**
     * 执行一次挂机脚本 - 在线组队模式
     *
     * @param start 开始图片路径
     * @param end   结束图片路径
     * @param job   job配置
     */
    @SneakyThrows
    private void executeOnceInTeamOnlineMod(String start, String end, String reward, OnmyojiJob<RoyalSoulConfig> job, Matcher matcher) {
        // 开始
        boolean success;
        do {
            success = matcher.clickBlocking(start, true, 1, false);
        } while (success);

        // 睡眠10s，不会有人能15s刷魂吧
        Thread.sleep(15000);

        // 领取奖励
        do {
            success = matcher.clickBlocking(reward, true, 1, false);
        } while (success);

        // 结束
        do {
            success = matcher.clickBlocking(end, true, 1, false);
        } while (success);
    }

    @Override
    public String getProcuderName() {
        return "御魂";
    }
}
