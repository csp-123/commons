package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.config.RoyalSoulConfig;
import com.commons.onmyoji.config.TanSuoConfig;
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
import org.springframework.ui.context.Theme;

import java.util.List;

/**
 * Title: 探索挂机脚本处理器 - 默认困28，手动勾自动轮换，尽量单刷  组队很麻烦，效率也不如单刷
 *  另外：记得开轮换
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 22:39
 */
@Component("TanSuo")
public class TanSuoProducer extends InstanceZoneBaseProducer<TanSuoConfig> {

    private static final Logger logger = LoggerFactory.getLogger(TanSuoProducer.class);

    ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    /**
     * 开始探索图片路径
     */
    private String startTanSuo;

    /**
     * 挑战怪物图片路径
     */
    private String challenge;

    /**
     * 挑战boss图片路径
     */
    private String challengeBoss;

    /**
     * 奖励
     */
    private String reward;

    /**
     * 结束
     */
    private String end;

    /**
     * 结界突破
     */
    private String demarcation;

    /**
     * 结界突破票满
     */
    private String fullDemarcation;

    /**
     * 关闭探索
     */
    private String closeTanSuo;

    /**
     * 结束探索
     */
    private String endTanSuo;

    /**
     * 确认结束探索
     */
    private String confirmEndTanSuo;

    /**
     * 移动点：借助阴阳师自动挑战的寻敌功能，移动后取消自动挑战，不进行挑战就不会消耗小纸人体力
     */
    private String movePoint;

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
    public void produce(OnmyojiJob<TanSuoConfig> job) {
        // 脚本执行次数
        Integer count = 0;
        threadLocal.set(count);

        // todo 4. 清理结界突破（9退4）

        // 返回至庭院
        //commonService.backToYard(job.getTeamType());
        // 从庭院进入探索

        // 配置： 层数、截图存放位置
        TanSuoConfig jobConfig = job.getConfig();
        String imgDirectory = System.getProperty("user.dir") + "\\" + jobConfig.imgPath + "\\";

        // 开始探索
        startTanSuo = imgDirectory + OnmyojiConstant.TAN_SUO_START_BUTTON;

        // 挑战怪物
        challenge = imgDirectory + OnmyojiConstant.TAN_SUO_CHALLENGE_BUTTON;
        // 挑战boss
        challengeBoss = imgDirectory + OnmyojiConstant.TAN_SUO_CHALLENGE_BOSS_BUTTON;
        // 奖励
        reward = imgDirectory + OnmyojiConstant.TAN_SUO_REWARD_BUTTON;
        // 结束
        end = imgDirectory + OnmyojiConstant.TAN_SUO_END_BUTTON;
        // 结界
        demarcation = imgDirectory + OnmyojiConstant.TAN_SUO_DEMARCATION_BUTTON;
        // 结界票满
        fullDemarcation = imgDirectory + OnmyojiConstant.TAN_SUO_FULL_DEMARCATION_BUTTON;
        //关闭探索
        closeTanSuo = imgDirectory + OnmyojiConstant.TAN_SUO_CLOSE_BUTTON;
        // 结束探索
        endTanSuo = imgDirectory + OnmyojiConstant.TAN_SUO_END_TANSUO_BUTTON;
        // 确认结束探索
        confirmEndTanSuo = imgDirectory + OnmyojiConstant.TAN_SUO_CONFIRM_END_TANSUO_BUTTON;
        // 移动点
        movePoint = imgDirectory + OnmyojiConstant.TAN_SUO_MOVE_POINT_BUTTON;

        // 图片匹配器
        Matcher matcher = new Matcher(width, height);

        // 处理挂机时长
        if (job.getHangUpType().getType().equals(HangUpTypeEnum.TIMES.getCode())) {
            // 限次
            for (int i = 1; i <= job.getHangUpType().getTimes(); i++) {
                executeOnce(job, matcher);
            }
        } else if (job.getHangUpType().getType().equals(HangUpTypeEnum.TIME.getCode())) {
            // 限时
            long endTime = System.currentTimeMillis() + 60 * 1000 * 1000;
            while (System.currentTimeMillis() <= endTime) {
                executeOnce(job, matcher);
            }
        } else if (job.getHangUpType().getType().equals(HangUpTypeEnum.FOREVER.getCode())) {
            // 不限
            while (true) {
                executeOnce(job, matcher);
            }
        }

        threadLocal.remove();

    }

    /**
     * 执行脚本
     *
     * @param job
     */
    private void executeOnce(OnmyojiJob<TanSuoConfig> job, Matcher matcher) {
        Integer count = threadLocal.get();
        if (count == null) {
            count = 1;
        }
        logger.info(String.format("=============执行第%s次挂机脚本，处理器：[%s]，组队类型：[%s]=============", count, getProcuderName(), TeamTypeEnum.find(job.getTeamType()).getDesc()));
        if (job.getTeamType().equals(TeamTypeEnum.SOLO.getCode())) {
            executeOnceInSoloMod(job, matcher);
        }
        if (job.getTeamType().equals(TeamTypeEnum.TEAM.getCode())) {
            executeOnceInTeamMod(job, matcher);
        }
        logger.info("=============执行结束=============");
        threadLocal.set(++count);
    }

    /**
     * 执行一次挂机脚本 - 单刷模式
     */
    @SneakyThrows
    private void executeOnceInSoloMod(OnmyojiJob<TanSuoConfig> job, Matcher matcher) {
        // todo 结界清理
//        if (isfullDemarcation()) {
//            closeTanSuo();
//            toSleep(1000);
//            openDemarcation();
//            toSleep(1000);
//            clearDemarcation();
//            closeDemarcation();
//            choseChapter28();
//        }


//         单击章节按钮
//        ImgMatcher.matchAndClick(chapter, 1, true);
        int count = 0;
        do {
          count = matcher.count(startTanSuo, false);
        } while (count != 1);
        // 单击开始探索
        matcher.clickFirst(startTanSuo, true, false);
        Thread.sleep(2000);
        while (matcher.count(challengeBoss, false) == 0) {
            logger.info("====暂未发现boss==");
            if (matcher.count(challenge, true) != 0) {
                logger.info("==小怪尚存==");
                // 挑战小怪
                matcher.clickFirst(challenge, true, true, false);
                while (matcher.count(reward, false) != 1);
                matcher.clickFirst(reward, true, false);
                while (matcher.count(end, false) != 1);
                matcher.clickFirst(end, true, false);
                Thread.sleep(1000);

            } else {
                logger.info("==移动==");
                // 场上无可挑战小怪
                // 移动  todo 目前是点击固定位置 803，507  后续看一下有没有更好的解决方案
                matcher.click(movePoint, true, false);
                matcher.click(803, 537, movePoint);
                //移动后停顿0.8秒，鼠标移动会停顿200-400毫秒，保证总耗时一秒以内， 避免自动挑战成功
                Thread.sleep(800);
                matcher.click(803, 537, movePoint);
                // 视角移动后停顿2s
                Thread.sleep(2000);
            }
            Thread.sleep(2000);

        }
        // 不挑战boss，可简化流程，不会出现多种情况，损失并不大。

        // 退出
        matcher.clickFirst(endTanSuo, true, false);
        Thread.sleep(1000);
        while (matcher.count(confirmEndTanSuo, false) != 1);
        matcher.clickFirst(confirmEndTanSuo, true, false);
        Thread.sleep(1000);

    }


    /**
     * 清理结界突破
     */
    private void clearDemarcation() {
        // todo

    }

    private void openDemarcation() {
        ImgMatcher.matchAndClick(demarcation, 1, true);
    }

    /**
     * 关闭探索
     */
    private void closeTanSuo() {
        ImgMatcher.matchAndClick(closeTanSuo, 1, true);
    }

    /**
     * 结界突破券是否已满
     *
     * @return
     */
    private boolean isfullDemarcation() {

        return ImgMatcher.match(fullDemarcation, 1);

    }



    /**
     * 执行一次挂机脚本 - 组队模式
     * 组队模式下，需要对屏幕内的所有命中位置进行点击，适配多开的情况
     *
     * @param job job配置
     */
    private void executeOnceInTeamMod(OnmyojiJob<TanSuoConfig> job, Matcher matcher) {
        // todo
    }

    @Override
    public String getProcuderName() {
        return "探索";
    }
}