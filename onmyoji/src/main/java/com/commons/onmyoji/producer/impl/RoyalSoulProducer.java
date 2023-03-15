package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.config.RoyalSoulConfig;
import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.enums.HangUpTypeEnum;
import com.commons.onmyoji.enums.TeamTypeEnum;
import com.commons.onmyoji.job.OnmyojiJob;
import com.commons.onmyoji.matcher.ImgMatcher;
import com.commons.onmyoji.producer.InstanceZoneBaseProducer;
import com.commons.onmyoji.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    ThreadLocal threadLocal = new ThreadLocal<>();

    /**
     * 开始图片路径
     */
    private String start;

    /**
     * 结束图片路径
     */
    private String end;

    /**
     * 获取奖励图片路径
     */
    private String reward;


    @Autowired
    CommonService commonService;
    

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
        Integer count = 0;
        threadLocal.set(count);
        // todo 1. 目前仅支持御魂界面开始挂机 庭院相关处理后续提供一个公共方法
        // todo 2. 组队逻辑待补全
        // todo 3. 容错机制：好友邀请悬赏

        // 返回至庭院
//        commonService.backToYard(job.getTeamType());
        // 从庭院进入御魂


        // 配置： 层数、截图存放位置
        RoyalSoulConfig jobConfig = job.getConfig();
        String imgDirectory = System.getProperty("user.dir") + "\\" + jobConfig.imgPath + "\\";


        // 根据组队类型 start end 赋值
        if (job.getTeamType().equals(TeamTypeEnum.SOLO.getCode())) {
            // solo
            start = imgDirectory + OnmyojiConstant.ROYAL_SOUL_SOLO_START_BUTTON;
            end = imgDirectory + OnmyojiConstant.ROYAL_SOUL_END_BUTTON;

        } else if (job.getTeamType().equals(TeamTypeEnum.TEAM.getCode())) {
            // 组队
            start = imgDirectory + OnmyojiConstant.ROYAL_SOUL_TEAM_START_BUTTON;
            end = imgDirectory + OnmyojiConstant.ROYAL_SOUL_TEAM_END_BUTTON;

        }
        // 奖励图片
        reward = imgDirectory + OnmyojiConstant.ROYAL_SOUL_REWARD_BUTTON;

        // 处理挂机时长
        if (job.getHangUpType().getType().equals(HangUpTypeEnum.TIMES.getCode())) {
            // 限次
            for (int i = 1; i <= job.getHangUpType().getTimes(); i++) {
                executeOnce(start, end, reward, job);
            }
        } else if (job.getHangUpType().getType().equals(HangUpTypeEnum.TIME.getCode())) {
            // 限时
            long endTime = System.currentTimeMillis() + 60 * 1000 * 1000;
            while (System.currentTimeMillis() <= endTime) {
                executeOnce(start, end, reward, job);
            }
        } else if (job.getHangUpType().getType().equals(HangUpTypeEnum.FOREVER.getCode())) {
            // 不限
            while (true) {
                executeOnce(start, end, reward, job);
            }
        }


    }

    /**
     * 执行脚本
     *
     * @param start
     * @param end
     * @param job
     */
    private void executeOnce(String start, String end, String reward, OnmyojiJob<RoyalSoulConfig> job) {
        int count = (Integer) threadLocal.get() + 1;
        logger.info(String.format("=============执行第%s次挂机脚本，处理器：[%s]，组队类型：[%s]=============", count, getProcuderName(), job.getTeamType(), TeamTypeEnum.find(job.getTeamType())));
        if (job.getTeamType().equals(TeamTypeEnum.SOLO.getCode())) {
            executeOnceInSoloMod(start, end, reward, job);
        }
        if (job.getTeamType().equals(TeamTypeEnum.TEAM.getCode())) {
            executeOnceInTeamMod(start, end, reward, job);
        }
        logger.info("=============执行结束=============");
        threadLocal.set(++count);
    }

    /**
     * 执行一次挂机脚本 - 单刷模式
     */
    private void executeOnceInSoloMod(String start, String end, String reward, OnmyojiJob<RoyalSoulConfig> job) {
        // 点击开始
        ImgMatcher.matchAndClick(start, 1, true);
        // 点击获得奖励
        ImgMatcher.matchAndClick(start, 1, true);
        // 点击结束
        ImgMatcher.matchAndClick(start, 1, true);
    }

    /**
     * 执行一次挂机脚本 - 组队模式
     * 组队模式下，需要对屏幕内的所有命中位置进行点击，适配多开的情况
     *
     * @param start 开始图片路径
     * @param end   结束图片路径
     * @param job   job配置
     */
    private void executeOnceInTeamMod(String start, String end, String reward, OnmyojiJob<RoyalSoulConfig> job) {
        // 点击开始
        ImgMatcher.matchAndClick(start, 1, true);
        // 点击获取奖励
        ImgMatcher.matchAndClick(reward, 2, true);
        // 点击结束
        ImgMatcher.matchAndClick(end, 2, true);
    }

    @Override
    public String getProcuderName() {
        return "御魂";
    }
}
