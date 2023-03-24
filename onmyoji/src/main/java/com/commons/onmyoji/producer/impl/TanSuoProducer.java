package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.config.RoyalSoulConfig;
import com.commons.onmyoji.config.TanSuoConfig;
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

import java.util.List;

/**
 * Title: 探索挂机脚本处理器 - 默认困28，手动勾自动轮换，尽量单刷  组队很麻烦，效率也不如单刷
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
     * 章节图片路径
     */
    private String chapter;

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

    private String move;

    private String move2;

    private String title;

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
        // todo 1. 目前仅支持御魂界面开始挂机 庭院相关处理后续提供一个公共方法
        // todo 2. 组队逻辑待补全
        // todo 3. 容错机制：好友邀请悬赏（完成） 宠物发现额外奖励、组队超市重新邀请

        // 返回至庭院
        //commonService.backToYard(job.getTeamType());
        // 从庭院进入探索

        // 配置： 层数、截图存放位置
        TanSuoConfig jobConfig = job.getConfig();
        String imgDirectory = System.getProperty("user.dir") + "\\" + jobConfig.imgPath + "\\";
        // todo 这里默认k28了，之后有需求再改吧（改为按配置里配置的层数来）
        chapter = imgDirectory + OnmyojiConstant.TAN_SUO_CHAPTER_K28_BUTTON;

        // 根据组队类型 start end 赋值
        if (job.getTeamType().equals(TeamTypeEnum.SOLO.getCode())) {
            // solo
            startTanSuo = imgDirectory + OnmyojiConstant.TAN_SUO_START_SOLO_BUTTON;
        } else if (job.getTeamType().equals(TeamTypeEnum.TEAM.getCode())) {
            // 组队
            startTanSuo = imgDirectory + OnmyojiConstant.TAN_SUO_START_TEAM_BUTTON;
            // todo 邀请逻辑
        }

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

        // 阴阳师图标
        title = imgDirectory + OnmyojiConstant.TAN_SUO_TITLE_BUTTON;
        // 处理挂机时长
        if (job.getHangUpType().getType().equals(HangUpTypeEnum.TIMES.getCode())) {
            // 限次
            for (int i = 1; i <= job.getHangUpType().getTimes(); i++) {
                executeOnce(job);
            }
        } else if (job.getHangUpType().getType().equals(HangUpTypeEnum.TIME.getCode())) {
            // 限时
            long endTime = System.currentTimeMillis() + 60 * 1000 * 1000;
            while (System.currentTimeMillis() <= endTime) {
                executeOnce(job);
            }
        } else if (job.getHangUpType().getType().equals(HangUpTypeEnum.FOREVER.getCode())) {
            // 不限
            while (true) {
                executeOnce(job);
            }
        }


    }

    /**
     * 执行脚本
     *
     * @param job
     */
    private void executeOnce(OnmyojiJob<TanSuoConfig> job) {
        int count = (Integer) threadLocal.get() + 1;
        logger.info(String.format("=============执行第%s次挂机脚本，处理器：[%s]，组队类型：[%s]=============", count, getProcuderName(), job.getTeamType(), TeamTypeEnum.find(job.getTeamType())));
        if (job.getTeamType().equals(TeamTypeEnum.SOLO.getCode())) {
            executeOnceInSoloMod(job);
        }
        if (job.getTeamType().equals(TeamTypeEnum.TEAM.getCode())) {
            executeOnceInTeamMod(job);
        }
        logger.info("=============执行结束=============");
        threadLocal.set(++count);
    }

    /**
     * 执行一次挂机脚本 - 单刷模式
     */
    private void executeOnceInSoloMod(OnmyojiJob<TanSuoConfig> job) {
        // todo 结界清理

//         单击章节按钮
//        ImgMatcher.matchAndClick(chapter, 1, true);
        // 单击开始探索
        ImgMatcher.matchAndClick(startTanSuo, 1, true);
        boolean moved = false;
        while (ImgMatcher.similarCount(challengeBoss) == 0) {

            if (ImgMatcher.similarCount(challenge) != 0) {
                // 挑战小怪
                ImgMatcher.similarMatchAndClickFirst(challenge, true);
                ImgMatcher.matchAndClick(reward, 1, true);
                ImgMatcher.matchAndClick(end, 1, true);
                moved = false;
            } else if (!moved && ImgMatcher.similarCount(challenge) == 0) {
                // 没有移动过且场上无可挑战小怪
                moved = true;
                boolean match = ImgMatcher.match(title, 1);
                if (match) {
                    List<ImgMatcher.MatchResult> result = ImgMatcher.getResult();
                    ImgMatcher.MatchResult matchResult = result.get(0);
                    // 点击右移
                    // y: 379 ~ 488  x:-340~570
                    ImgMatcher.click(matchResult.getLocationX() + 115, matchResult.getLocationY() + 433, 910, 109, true);
                    // 移动后等待4秒
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (moved && ImgMatcher.similarCount(challenge) == 0) {
                // 移动过且场上无可挑战小怪
                moved = false;
            }

        }
        // 挑战boss
        ImgMatcher.similarMatchAndClickFirst(challengeBoss, true);
        ImgMatcher.matchAndClick(reward, 1, true);
        ImgMatcher.matchAndClick(end, 1, true);

    }

    /**
     * 执行一次挂机脚本 - 组队模式
     * 组队模式下，需要对屏幕内的所有命中位置进行点击，适配多开的情况
     *
     * @param job   job配置
     */
    private void executeOnceInTeamMod(OnmyojiJob<TanSuoConfig> job) {
        // todo
    }

    @Override
    public String getProcuderName() {
        return "探索";
    }
}
