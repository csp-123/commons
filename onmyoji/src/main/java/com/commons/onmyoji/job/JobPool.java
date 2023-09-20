package com.commons.onmyoji.job;

import com.commons.onmyoji.components.GameWindowFreshTask;
import com.commons.onmyoji.components.MouseOperateTask;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.SetUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
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
public class JobPool implements KeyListener {


    /**
     * 任务集
     */
    Map<String, OnmyojiJob> jobMap;

    /**
     * 运行中的任务集
     */
    Map<String, OnmyojiJob> runningJobMap;

    private final JobLoader jobLoader;

    @Resource
    private GameWindowFreshTask gameWindowFreshTask;

    @Resource
    private MouseOperateTask mouseOperateTask;

    private final Timer gameWindowFreshTimer = new Timer();

    private final Timer mouseTimer = new Timer();

    public JobPool(JobLoader jobLoader) {
        this.jobLoader = jobLoader;
    }

    @PostConstruct
    public void initJob(){
        jobMap = jobLoader.loadAllJobs();
        run();
    }

    public void run() {
        boolean isRunning = false;
        while (true) {
            if (CollectionUtils.isEmpty(runningJobMap)) {
                if (isRunning) {
                    gameWindowFreshTimer.cancel();
                    mouseTimer.cancel();
                    isRunning = false;
                }
                continue;
            }

            for (OnmyojiJob job : runningJobMap.values()) {
                if (!isRunning) {
                    //持续运行【屏幕刷新器】
                    gameWindowFreshTask.setWindowsNameList(Sets.newHashSet(job.getConfig().getWindowNameList()));
                    gameWindowFreshTimer.schedule(gameWindowFreshTask, new Date(), 500);
                    //持续运行【匹配结果点击器】
                    mouseTimer.schedule(mouseOperateTask, new Date(), 500);
                    isRunning = true;
                }
                job.start();
            }
        }
    }

    public void runJob(String id) {
        OnmyojiJob job = jobMap.get(id);
        runningJobMap.put(id, job);
    }

    public void stop(String id) {
        runningJobMap.remove(id);
    }

    public void stopAll() {
        runningJobMap.clear();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * ctrl 退出
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown()) {
            stopAll();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
