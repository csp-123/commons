package com.commons.onmyoji.job;

import com.commons.onmyoji.entity.OnmyojiJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
public class JobPool {


    /**
     * 任务集
     */
    Map<String, OnmyojiJob> jobMap;

    @Resource
    private JobLoader jobLoader;


    public JobPool(JobLoader jobLoader) {
        this.jobLoader = jobLoader;
    }

    @PostConstruct
    public void initJob(){
        jobMap = jobLoader.loadAllJobs();
    }

    public OnmyojiJob getJobById(String jobId) {
        return jobMap.get(jobId);
    }
}
