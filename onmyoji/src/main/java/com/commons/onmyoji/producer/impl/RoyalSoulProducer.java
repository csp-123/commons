package com.commons.onmyoji.producer.impl;

import com.alibaba.fastjson.JSON;
import com.commons.onmyoji.config.RoyalSoulConfig;
import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.job.OnmyojiJob;
import com.commons.onmyoji.producer.InstanceZoneBaseProducer;
import com.commons.onmyoji.service.CommonService;
import com.commons.onmyoji.utils.FindRobot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

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
     *                 1. 单刷 or 组队？
     *                 2. 限时 or 限次 or 刷到死？
     *             容错机制：
     *                 1. 宠物发现额外奖励
     *                 2. 组队时队友超时，重新邀请队友
     * @param job
     */
    @Override
    public void produce(OnmyojiJob<RoyalSoulConfig> job) {
        // todo 1. 目前仅支持御魂界面开始挂机 庭院相关处理后续提供一个公共方法
        // todo 2. 组队逻辑待补全
//        // 当前位置是否为庭院
//        boolean inYard = commonService.isInYardNow();
//        boolean inYardNow = inYard;
//        // 返回至庭院
//        while (!inYardNow) {
//            commonService.backToUpper();
//            inYardNow = commonService.isInYardNow();
//        }
//        // 从庭院进入御魂副本
//        if (inYard) {
//            // todo
//        }

        // 配置： 层数、截图存放位置
        RoyalSoulConfig jobConfig = job.getConfig();


        // 处理组队类型
        String imgDirectory = System.getProperty("user.dir") + "\\" + jobConfig.imgPath + "\\";
        if (job.getTeamType() == 1) {
            // solo
            start = imgDirectory + OnmyojiConstant.ROYAL_SOUL_SOLO_START_BUTTON;
            end = imgDirectory + OnmyojiConstant.ROYAL_SOUL_END_BUTTON;

        } else if (job.getTeamType() == 2) {
            // 组队
            start = imgDirectory + OnmyojiConstant.ROYAL_SOUL_TEAM_START_BUTTON;
            end = imgDirectory + OnmyojiConstant.ROYAL_SOUL_TEAM_END_BUTTON;

        }
        reward = imgDirectory + OnmyojiConstant.ROYAL_SOUL_REWARD_BUTTON;
        // 处理挂机时长
        if (job.getHangUpType().getType() == 2) {
            // 限次
            for (int i = 1; i <= job.getHangUpType().getTimes(); i++) {
                excuteOnce(start, end, reward, job);
            }
        } else if (job.getHangUpType().getType() == 1) {
            // 限时
            long endTime = new Date().getTime() + 60 * 1000 * 1000;
            if (new Date().getTime() <= endTime) {
                excuteOnce(start, end, reward, job);
            }
        } else if (job.getHangUpType().getType() == 3) {
            // 不限
            while (true) {
                excuteOnce(start, end, reward, job);
            }
        }



    }

    /**
     * 执行脚本
     * @param start
     * @param end
     * @param job
     */
    private void excuteOnce(String start, String end, String reward, OnmyojiJob<RoyalSoulConfig> job) {
        if (job.getTeamType() == 1) {
            excuteOnceInSoloMod(start, end, reward, job);
        }
        if (job.getTeamType() == 2) {
            excuteOnceInTeamMod(start, end, reward, job);
        }
    }

    /**
     * 执行一次挂机脚本 - 单刷模式
     */
    private void excuteOnceInSoloMod(String start, String end, String reward, OnmyojiJob job) {

        logger.info(String.format("======开始执行一次单刷挂机脚本,处理器：[%s],挂机任务配置：[%s}]", getProcuderName(), JSON.toJSON(job)));
        boolean foundStart = false;
        do {
            foundStart = FindRobot.touchPic(start);
        } while (!foundStart);
        //扫描至匹配到目标图片
        boolean foundEnd = false;
        do {
            foundEnd = FindRobot.findPoint(reward, false);
        } while (!foundEnd);
        // 点击获取奖励
        FindRobot.touchPic(reward);
        // 点击结束
        FindRobot.touchPic(end);
        logger.info("===执行结束===");


    }

    /**
     * 执行一次挂机脚本 - 组队模式
     *      组队模式下，需要对屏幕内的所有命中位置进行点击，适配多开的情况
     * @param start
     * @param end
     * @param job
     */
    private void excuteOnceInTeamMod(String start, String end, String reward, OnmyojiJob job) {
        logger.info(String.format("======开始执行一次挂机脚本,处理器：[%s],挂机任务配置：[%s}]", getProcuderName(), JSON.toJSON(job)));
        boolean foundStart = false;
        do {
            foundStart = FindRobot.touchPic(start);
        } while (!foundStart);
        //有两个匹配结果时再执行点击
        do {
            FindRobot findRobot = new FindRobot(reward, null, 0, 0);
        } while (FindRobot.map.size() <= 1);
        // 点击所有获取奖励
        FindRobot.touchAllPic(reward);
        //有两个匹配结果时再执行点击
        do {
            FindRobot findRobot = new FindRobot(end, null, 0, 0);
        } while (FindRobot.map.size() <= 1);
        // 点击所有结束
        FindRobot.touchAllPic(end);
        logger.info("===执行结束===");
    }

    @Override
    public String getProcuderName() {
        return "御魂";
    }
}
