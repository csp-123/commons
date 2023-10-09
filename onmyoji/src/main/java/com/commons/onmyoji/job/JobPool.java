package com.commons.onmyoji.job;

import com.commons.onmyoji.components.GameWindowFreshTask;
import com.commons.onmyoji.components.MouseOperateTask;
import com.commons.onmyoji.components.OnmyojiDaemonTask;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

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


    /**
     * 任务集
     */
    Map<String, OnmyojiJob> jobMap;

    private final JobLoader jobLoader;

    @Resource
    private GameWindowFreshTask gameWindowFreshTask;

    @Resource
    private MouseOperateTask mouseOperateTask;

    @Resource
    private OnmyojiDaemonTask daemonTask;

    private final Timer gameWindowFreshTimer = new Timer();

    private final Timer mouseTimer = new Timer();

    private final Timer daemonTimer = new Timer();

    public JobPool(JobLoader jobLoader) {
        this.jobLoader = jobLoader;
    }

    @PostConstruct
    public void initJob(){
        jobMap = jobLoader.loadAllJobs();
    }

    public void runJob(String id) {
        OnmyojiJob job = jobMap.get(id);
        //持续运行【屏幕刷新器】
        gameWindowFreshTask.setWindowsNameList(Sets.newHashSet(job.getConfig().getWindowNameList()));
        gameWindowFreshTimer.schedule(gameWindowFreshTask, new Date(), 200);
        //持续运行【匹配结果点击器】
//        mouseTimer.schedule(mouseOperateTask, new Date(), 500);
        daemonTimer.schedule(daemonTask, new Date(), 200);
        job.start();
        gameWindowFreshTimer.cancel();
        daemonTimer.cancel();
    }

    public void stopAll() {
//        mouseTimer.cancel();
        log.info("正在结束任务");
        gameWindowFreshTimer.cancel();
        daemonTimer.cancel();
        for (OnmyojiJob value : jobMap.values()) {
            value.stop();
        }
        log.info("任务调度已结束");
    }

}
