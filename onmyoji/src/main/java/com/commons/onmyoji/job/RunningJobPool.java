package com.commons.onmyoji.job;

import com.commons.onmyoji.entity.OnmyojiJob;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Title:
 * Description:
 * Project: gov-common
 * Author: csp
 * Create Time:2023/2/21 23:12 下午
 */
@Component
@Slf4j
@Data
public class RunningJobPool {


    /**
     * 运行任务集
     */
    Map<String, OnmyojiJob> runningJobMap;

    @PostConstruct
    public void initJob(){
        runningJobMap = new HashMap<>();
    }


    public void addJob(OnmyojiJob job) {
        runningJobMap.put(job.getJobId(), job);
    }

    public void removeJob(OnmyojiJob job) {
        runningJobMap.remove(job.getJobId());
    }

    public void removeJob(String jobId) {
        runningJobMap.remove(jobId);
    }

    public void removeAll() {
        runningJobMap.clear();
    }

    public OnmyojiJob getJob(String jobId) {
        return runningJobMap.get(jobId);
    }

    public Boolean containsJob(String jobId) {
        return runningJobMap.containsKey(jobId);
    }

    public void stopAll() {
        runningJobMap.clear();
    }
}
