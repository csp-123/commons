package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.components.GameWindowFreshTask;
import com.commons.onmyoji.components.JobStatusFreshTask;
import com.commons.onmyoji.components.Matcher;

import com.commons.onmyoji.components.MouseOperateTask;
import com.commons.onmyoji.constant.OnmyojiConstant;

import com.commons.onmyoji.entity.OnmyojiJob;
import com.commons.onmyoji.job.RunningJobPool;
import com.commons.onmyoji.producer.InstanceZoneProducer;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Title: 通用处理器
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 22:39
 */
@Component("Common")
@Slf4j
public class CommonProducer implements InstanceZoneProducer {

    @Resource
    Matcher matcher;

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
            List<String> windowsNameList = Arrays.asList(job.getWindowNameList().split(","));
            // 持续刷新屏幕RGB数据
            gameWindowFreshTask.setWindowsNameList(Sets.newHashSet(job.getWindowNameList()));
            gameWindowFreshExecutor.scheduleAtFixedRate(gameWindowFreshTask, 0, 500, TimeUnit.MILLISECONDS);
            // 持续检查任务是否完成
            checkJobDoneExecutor.scheduleAtFixedRate(jobStatusFreshTask, 0, 5, TimeUnit.SECONDS);
            // 匹配器工作
            matcher.init(job.getImgDirectory(), windowsNameList);
            matcher.start(job.getJobId());
        } catch (Exception e) {
            log.error(Throwables.getStackTraceAsString(e));
        } finally {
            gameWindowFreshExecutor.shutdown();
            checkJobDoneExecutor.shutdown();
        }
    }

    @Override
    public String getProducerName() {
        return "通用";
    }


}
