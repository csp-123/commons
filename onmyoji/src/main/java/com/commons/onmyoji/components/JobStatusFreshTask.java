package com.commons.onmyoji.components;

import com.alibaba.fastjson.JSON;
import com.commons.onmyoji.entity.*;
import com.commons.onmyoji.enums.HangUpTypeEnum;
import com.commons.onmyoji.job.RunningJobMatchResultPool;
import com.commons.onmyoji.job.RunningJobPool;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.*;


/**
 * 任务状态刷新任务：持续监测任务是否已完成
 *
 * @author chishupeng
 * @date 2023/8/18 2:45 PM
 */
@Data
@Slf4j
@Component
@EqualsAndHashCode(callSuper = true)
public class JobStatusFreshTask extends TimerTask {

    @Resource
    RunningJobPool runningJobPool;

    @Resource
    RunningJobMatchResultPool runningJobMatchResultPool;

    @Override
    public void run() {
        Map<String, OnmyojiJob> runningJobMap = runningJobPool.getRunningJobMap();
        if (runningJobMap.isEmpty()) {
            return;
        }
        for (OnmyojiJob job : runningJobMap.values()) {
            checkJobDone(job);
        }
    }

    private void checkJobDone(OnmyojiJob job) {
        // TIMES
        if (HangUpTypeEnum.TIMES.equals(job.getHangUpType())) {
            MatchResult matchResult = runningJobMatchResultPool.get(job.getJobId());
            if (matchResult == null) {
                log.info("【任务状态监测】[任务：{}，是否完成：{}]", job.getJobName(), false);
                return;
            }
            Map<String, Set<TargetMatchingResult>> resultItemMap = matchResult.getResultItemMap();
            for (Map.Entry<String, Set<TargetMatchingResult>> entry : resultItemMap.entrySet()) {
                // 任意图片匹配次数达标即为完成
                Integer maxMatchCount = entry.getValue().stream().map(TargetMatchingResult::getCount).max(Comparator.comparingInt(o -> o)).orElse(0);
                if (job.getTimes() <= maxMatchCount) {
                    runningJobPool.removeJob(job);
                }
                log.info("【任务状态监测】[任务：{}，当前进度：{}/{}]", job.getJobName(), maxMatchCount, job.getTimes());
            }
        }
        // TIME_FROM_NOW
        if (HangUpTypeEnum.TIME_FROM_NOW.equals(job.getHangUpType())) {
            LocalDateTime deadLine = job.getExecuteTime().plusMinutes(job.getTime());
            log.info("deadLine:{}", deadLine);
            // 到期
            if (LocalDateTime.now().isAfter(deadLine)) {
                runningJobPool.removeJob(job);
                log.info("【任务状态监测】[任务：{}，剩余运行时间：0， 即将退出]", job.getJobName());
                return;
            }
            log.info("【任务状态监测】[任务：{}，剩余运行时间（分钟）：{}]", job.getJobName(), deadLine.getMinute() - LocalDateTime.now().getMinute());
        }
    }


}
