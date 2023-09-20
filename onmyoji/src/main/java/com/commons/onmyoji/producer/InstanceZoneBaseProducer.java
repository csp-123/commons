package com.commons.onmyoji.producer;

import com.commons.onmyoji.components.Matcher;
import com.commons.onmyoji.components.Matcher0920;
import com.commons.onmyoji.config.OnmyojiScriptConfig;
import com.commons.onmyoji.config.RoyalSoulConfig;
import com.commons.onmyoji.entity.MatchResult;
import com.commons.onmyoji.enums.HangUpTypeEnum;
import com.commons.onmyoji.job.HangUpType;
import com.commons.onmyoji.job.OnmyojiJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Title:
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 23:01
 */
@Component
@Slf4j
public abstract class InstanceZoneBaseProducer<CONFIG extends OnmyojiScriptConfig> implements InstanceZoneProducer<CONFIG> {

    @Resource
    Matcher0920 matcher0920;

    /**
     * 预处理
     */
    public abstract void prepare(OnmyojiJob<CONFIG> job);

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
    public void produce(OnmyojiJob job) {
        prepare(job);
        HangUpType hangUpType = job.getHangUpType();
        // 限次
        if (hangUpType.getType().equals(HangUpTypeEnum.TIMES.getCode())) {
            boolean stop;
            do {
                Collection<Integer> countValues = MatchResult.getInstance().getClickCountMap().values();
                stop = countValues.stream().allMatch(it -> it.compareTo(hangUpType.getTimes()) >= 0);
                matcher0920.matchAll(job.isSolo());
            } while (!stop);
        }
        //  限时
        if (hangUpType.getType().equals(HangUpTypeEnum.TIME.getCode())) {
            long endTime;
            if (Objects.nonNull(hangUpType.getUnit())) {
                endTime = System.currentTimeMillis() + hangUpType.getUnit().toMillis(hangUpType.getTime());
            } else {
               endTime = System.currentTimeMillis() + hangUpType.getTime() * 60 * 1000L;
            }
            boolean stop;
            do {
                stop = System.currentTimeMillis() == endTime;
                matcher0920.matchAll(job.isSolo());
            } while (!stop);
        }
        // 不限
        if (hangUpType.getType().equals(HangUpTypeEnum.FOREVER.getCode())) {
            while (true) {
                matcher0920.matchAll(job.isSolo());
            }
        }
    }
}
