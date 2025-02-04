package com.commons.onmyoji.web;

import com.alibaba.fastjson.JSON;
import com.commons.onmyoji.entity.OnmyojiJob;
import com.commons.onmyoji.job.JobPool;
import com.commons.onmyoji.job.RunningJobPool;
import com.commons.onmyoji.producer.InstanceZoneProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Title:
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 23:45
 */
@RestController
@RequestMapping("job")
@Slf4j
public class WebController {

    @Resource
    JobPool jobPool;

    @Resource
    RunningJobPool runningJobPool;

    @Resource
    ApplicationContext applicationContext;


    @GetMapping("/runByJobId")
    public void runByJobId(String jobId){
        OnmyojiJob job = jobPool.getJobById(jobId);
        InstanceZoneProducer producer = (InstanceZoneProducer)applicationContext.getBean(job.getProducerName());
        producer.produce(job);
    }

    @GetMapping("/stopByJobId")
    public void stopByJobId(String jobId){
        if (!runningJobPool.containsJob(jobId)) {
            return;
        }
        runningJobPool.removeJob(jobId);
    }

    @GetMapping("/stopAll")
    public void stopAll(){
        runningJobPool.stopAll();
    }
}
