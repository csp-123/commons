package com.commons.onmyoji.job;

import com.commons.onmyoji.components.GameWindowFreshTask;
import com.commons.onmyoji.components.MouseOperateTask;
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
public class JobPool {
    

    Map<String, OnmyojiJob> jobMap;

    private final JobLoader jobLoader;

    @Resource
    private GameWindowFreshTask gameWindowFreshTask;

    @Resource
    private MouseOperateTask mouseOperateTask;

    private static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(2);

    public JobPool(JobLoader jobLoader) {
        this.jobLoader = jobLoader;
    }

    @PostConstruct
    public void initJob(){
        jobMap = jobLoader.loadAllJobs();
    }

    public void runJob(String id){
        OnmyojiJob job = jobMap.get(id);
        // 屏幕每秒刷新一次
        gameWindowFreshTask.setWindowsNameList(job.getConfig().getWindowNameList());
        scheduledExecutor.scheduleAtFixedRate(gameWindowFreshTask, 0, 1, TimeUnit.SECONDS);
        // 间隔1秒检查一次匹配结果，有则点击，无则跳过
        scheduledExecutor.scheduleWithFixedDelay(mouseOperateTask, 0, 1, TimeUnit.SECONDS);

        job.start();
    }
}
