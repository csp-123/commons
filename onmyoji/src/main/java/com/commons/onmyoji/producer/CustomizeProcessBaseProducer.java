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
 * Title:定制流程处理器
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 23:01
 */
@Component
@Slf4j
@Getter
@Setter
public abstract class CustomizeProcessBaseProducer<CONFIG extends OnmyojiScriptConfig> implements InstanceZoneProducer<CONFIG> {

    private boolean toBeStop = false;

    public abstract void process(OnmyojiJob<CONFIG> job);


    public void produce(OnmyojiJob job) {

        HangUpType hangUpType = job.getHangUpType();
        // 限次
        if (hangUpType.getType().equals(HangUpTypeEnum.TIMES.getCode())) {
            Integer hangUpTypeTimes = hangUpType.getTimes();
            int times = 0;
            boolean stop;
            do {
                process(job);
                times++;
                stop = times>=hangUpTypeTimes || toBeStop;
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
                process(job);
            } while (!stop);
        }
        // 不限
        if (hangUpType.getType().equals(HangUpTypeEnum.FOREVER.getCode())) {
            while (!toBeStop) {
                process(job);
            }
        }
    }

    @Override
    public void stop() {
        this.setToBeStop(true);
    }
}
