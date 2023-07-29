package com.commons.onmyoji.web;

import com.commons.onmyoji.job.JobPool;
import org.springframework.beans.factory.annotation.Autowired;
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
public class WebController {

    @Resource
    JobPool jobPool;

    @GetMapping("/run")
    public void run(String id){
        jobPool.runJob(id);
    }
}
