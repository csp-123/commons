package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.components.*;
import com.commons.onmyoji.entity.OnmyojiJob;
import com.commons.onmyoji.entity.Picture;
import com.commons.onmyoji.job.RunningJobPool;
import com.commons.onmyoji.producer.InstanceZoneProducer;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Title: 通用处理器v2
 * Description:
 * 通用处理逻辑：
 *  1. 匹配start图片，获取在屏幕上的位置，点击，鼠标随机斜上移（人类习惯）
 *  2. （屏幕刷新器会每秒刷新屏幕数据）
 *  3. 轮询匹配其余图片，如果匹配到图片，不关心具体位置，均点击当前鼠标位置，并随机多次点击（人类习惯）
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 22:39
 */
@Component("CommonV2")
@Slf4j
public class CommonProducerV2 implements InstanceZoneProducer {

    @Resource
    MatcherV2 matcherV2;

    @Resource
    RunningJobPool runningJobPool;

    @Resource
    GameWindowFreshTask gameWindowFreshTask;

    @Resource
    JobStatusFreshTask jobStatusFreshTask;


    @Override
    public void produce(OnmyojiJob job) {
        ScheduledThreadPoolExecutor gameWindowFreshExecutor = new ScheduledThreadPoolExecutor(2);
        ScheduledThreadPoolExecutor checkJobDoneExecutor = new ScheduledThreadPoolExecutor(2);

        try {
            job.setExecuteTime(LocalDateTime.now());
            runningJobPool.addJob(job);

            // 运行屏幕刷新器
            gameWindowFreshTask.setWindowsNameList(Sets.newHashSet(job.getWindowNameList()));
            gameWindowFreshExecutor.scheduleAtFixedRate(gameWindowFreshTask, 0, 500, TimeUnit.MILLISECONDS);
            // 运行任务检查器
            checkJobDoneExecutor.scheduleAtFixedRate(jobStatusFreshTask, 0, 5, TimeUnit.SECONDS);
            // 加载截图
            job.loadPictures();
            // 循环执行，什么时间结束交给checkJobDoneExecutor判断
            while (runningJobPool.containsJob(job.getJobId())) {
                matcherV2.run(job);
            }
        } catch (Exception e) {
            log.error(Throwables.getStackTraceAsString(e));
        } finally {
            gameWindowFreshExecutor.shutdown();
            checkJobDoneExecutor.shutdown();
        }
    }

    @Override
    public String getProducerName() {
        return "通用V2";
    }


}
