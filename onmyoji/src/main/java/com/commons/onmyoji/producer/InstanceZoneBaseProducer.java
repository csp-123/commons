package com.commons.onmyoji.producer;

import com.commons.onmyoji.components.Matcher;
import com.commons.onmyoji.config.OnmyojiScriptConfig;
import com.commons.onmyoji.enums.HangUpTypeEnum;
import com.commons.onmyoji.job.HangUpType;
import com.commons.onmyoji.job.OnmyojiJob;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Objects;

/**
 * Title:
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 23:01
 */
@Component
@Slf4j
@Getter
@Setter
public abstract class InstanceZoneBaseProducer<CONFIG extends OnmyojiScriptConfig> implements InstanceZoneProducer<CONFIG> {

    @Resource
    Matcher matcher;

    private boolean toBeStop = false;

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
            boolean stop = false;
            do {
                Collection<Integer> countValues = matcher.getMatchResult().getClickCountMap().values();
                if (!CollectionUtils.isEmpty(countValues)) {
                    stop = countValues.stream().allMatch(it -> it.compareTo(hangUpType.getTimes()) >= 0) || toBeStop;
                }
                matcher.matchAll(job.isSolo());
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
                long now = System.currentTimeMillis();
                stop = toBeStop || now >= endTime;
                matcher.matchAll(job.isSolo());
            } while (!stop);
        }
        // 不限
        if (hangUpType.getType().equals(HangUpTypeEnum.FOREVER.getCode())) {
            while (!toBeStop) {
                matcher.matchAll(job.isSolo());
            }
        }
    }

    @Override
    public void stop() {
        this.setToBeStop(true);
    }
}
