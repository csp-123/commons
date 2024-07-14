package com.commons.onmyoji.job;

import com.commons.onmyoji.entity.OnmyojiJob;
import com.commons.onmyoji.enums.HangUpTypeEnum;
import com.commons.onmyoji.enums.TeamTypeEnum;
import com.commons.onmyoji.loader.YmlLoader;
import com.commons.onmyoji.producer.InstanceZoneProducer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
    private final Map<String, InstanceZoneProducer> producerMap;

    private Map<String, OnmyojiJob> jobs;

    @Resource
    private YmlLoader ymlLoader;


    public JobLoader(Map<String, InstanceZoneProducer> producerMap, YmlLoader ymlLoader) {
        this.producerMap = producerMap;
        this.ymlLoader = ymlLoader;
    }

    @SneakyThrows
    public Map<String, OnmyojiJob> loadAllJobs() {
        log.info("==============加载任务列表=============");
        log.info("");
        if (jobs == null) {
            List<PropertiesPropertySource> sources = ymlLoader.loadAllYml("classpath:job/*.yml");
            jobs = sources.stream().map(this::parseJob).collect(Collectors.toMap(OnmyojiJob::getJobId, r -> r));
        }
        log.info("==============任务加载完毕=============");

        return jobs;
    }


    /**
     * Job解析
     * @param source
     * @return
     */
    private OnmyojiJob parseJob(PropertiesPropertySource source) {
        log.info("开始加载任务文件：{}", source.getName().replace(".yml", ""));
        log.info("任务名称：{}", (String) source.getProperty("jobName"));
        OnmyojiJob job = new OnmyojiJob();
        job.setJobId((String)source.getProperty("jobId"));
        job.setJobName((String)source.getProperty("jobName"));
        job.setTeamType(TeamTypeEnum.findByType((String) source.getProperty("teamType")));
        job.setHangUpType(HangUpTypeEnum.findByType((String) source.getProperty("hangUpType")));
        job.setTimes((Integer)source.getProperty("times"));
        job.setTime((Integer)source.getProperty("time"));
        job.setHangUpType(HangUpTypeEnum.findByType((String) source.getProperty("hangUpType")));
        job.setProducerName((String)source.getProperty("producerName"));
        job.setTerminal((String)source.getProperty("terminal"));
        job.setWindowNameList((String)source.getProperty("windowNameList"));
        return job;
    }

}
