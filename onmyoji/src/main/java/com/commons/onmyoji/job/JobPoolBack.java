//package com.commons.onmyoji.job;
//
//import com.commons.onmyoji.components.GameWindowFreshTask;
//import com.commons.onmyoji.components.MouseOperateTask;
//import com.commons.onmyoji.components.OnmyojiDaemonTask;
//import com.commons.onmyoji.entity.OnmyojiJob;
//import com.commons.onmyoji.producer.InstanceZoneProducer;
//import com.google.common.collect.Sets;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import java.util.Date;
//import java.util.Map;
//import java.util.Timer;
//
///**
// * Title:
// * Description:
// * Project: gov-common
// * Author: csp
// * Create Time:2023/2/21 23:12 下午
// */
//@Component
//@Slf4j
//public class JobPoolBack {
//
//
//    /**
//     * 任务集
//     */
//    Map<String, OnmyojiJob> jobMap;
//
//    @Resource
//    private JobLoader jobLoader;
//
//    @Resource
//    private GameWindowFreshTask gameWindowFreshTask;
//
//    @Resource
//    private MouseOperateTask mouseOperateTask;
//
//    @Resource
//    private OnmyojiDaemonTask daemonTask;
//
//    @Resource
//    private ApplicationContext context;
//
//    public JobPoolBack(JobLoader jobLoader) {
//        this.jobLoader = jobLoader;
//    }
//
//    @PostConstruct
//    public void initJob(){
//        jobMap = jobLoader.loadAllJobs();
//    }
//
//    public void runJob(String id) {
//        OnmyojiJob job = jobMap.get(id);
//        //持续运行【屏幕刷新器】
//        gameWindowFreshTask.setWindowsNameList(Sets.newHashSet(job.getConfig().getWindowNameList()));
//        Timer gameWindowFreshTimer = new Timer();
//        gameWindowFreshTimer.schedule(gameWindowFreshTask, new Date(), 20);
//        //持续运行【匹配结果点击器】
////        mouseTimer.schedule(mouseOperateTask, new Date(), 500);
//        Timer daemonTimer = new Timer();
//        daemonTimer.schedule(daemonTask, new Date(), 5000);
//        // 处理器
//        InstanceZoneProducer producer = (InstanceZoneProducer)context.getBean(job.getProducerName());
//        producer.produce(job);
//        gameWindowFreshTimer.cancel();
//        daemonTimer.cancel();
//    }
//
//    public void stopAll() {
////        mouseTimer.cancel();
//        log.info("正在结束任务");
//        gameWindowFreshTimer.cancel();
//        daemonTimer.cancel();
//        for (OnmyojiJob value : jobMap.values()) {
//            value.stop();
//        }
//        log.info("任务调度已结束");
//    }
//
//}
