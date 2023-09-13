package com.commons.onmyoji.job;

import com.commons.onmyoji.components.GameWindowFreshTask;
import com.commons.onmyoji.components.MouseOperateTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Title:
 * Description:
 * Project: gov-common
 * Author: csp
 * Create Time:2023/2/21 23:12 下午
 */
@Component
@Slf4j
public class JobPool {
    

    Map<String, OnmyojiJob> jobMap;

    private final JobLoader jobLoader;

    @Resource
    private GameWindowFreshTask gameWindowFreshTask;

    @Resource
    private MouseOperateTask mouseOperateTask;

    public JobPool(JobLoader jobLoader) {
        this.jobLoader = jobLoader;
    }

    @PostConstruct
    public void initJob(){
        jobMap = jobLoader.loadAllJobs();
    }

    public void runJob(String id){
        OnmyojiJob job = jobMap.get(id);
        Timer gameWindowFreshTimer = new Timer();
        Timer mouseTimer = new Timer();
        //持续运行【屏幕刷新器】
        gameWindowFreshTask.setWindowsNameList(job.getConfig().getWindowNameList());
        gameWindowFreshTimer.schedule(gameWindowFreshTask, new Date(), 500);
        //持续运行【匹配结果点击器】
        mouseTimer.schedule(mouseOperateTask, new Date(), 500);
        job.start();
    }
}
