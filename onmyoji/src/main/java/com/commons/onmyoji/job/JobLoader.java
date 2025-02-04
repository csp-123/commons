package com.commons.onmyoji.job;

import cn.hutool.setting.yaml.YamlUtil;
import com.alibaba.fastjson.JSON;
import com.commons.onmyoji.entity.OnmyojiJob;
import com.commons.onmyoji.enums.HangUpTypeEnum;
import com.commons.onmyoji.enums.TeamTypeEnum;
import com.commons.onmyoji.loader.YmlLoader;
import com.commons.onmyoji.producer.InstanceZoneProducer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Title:
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/20 16:31
 */
@Component
@Slf4j
public class JobLoader {

    private Map<String, OnmyojiJob> jobs;


    /**
     * 加载任务列表
     * @return
     * @throws IOException
     */
    public Map<String, OnmyojiJob> loadAllJobs() throws IOException {
        log.info("==============加载任务列表=============");
        if (jobs == null) {
            List<OnmyojiJob> jobList = new ArrayList<>();
            // 指定目录路径
            String directoryPath = System.getProperty("user.dir") + "\\onmyoji\\src\\main\\resources\\job\\";
            File directory = new File(directoryPath);
            // 过滤YML文件
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    OnmyojiJob job = YamlUtil.load(Files.newInputStream(file.toPath()), OnmyojiJob.class);
                    jobList.add(job);
                }
            } else {
                throw new FileNotFoundException("Directory does not exist or is not readable.");
            }
            jobs = jobList.stream().peek(o -> log.info("已加载任务：{}", o.getJobName())).collect(Collectors.toMap(OnmyojiJob::getJobId, r -> r));
        }
        log.info("==============任务加载完毕=============");
        return jobs;
    }

}
