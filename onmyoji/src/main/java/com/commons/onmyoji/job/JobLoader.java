package com.commons.onmyoji.job;

import cn.hutool.setting.yaml.YamlUtil;
import com.commons.onmyoji.entity.OnmyojiJob;
import com.commons.onmyoji.enums.HangUpTypeEnum;
import com.commons.onmyoji.enums.TeamTypeEnum;
import com.google.protobuf.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
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
    public Map<String, OnmyojiJob> loadAllJobs() throws IOException, ServiceException {
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
                    job.loadPictures();
                    checkJob(job);
                    jobList.add(job);
                }
            } else {
                throw new FileNotFoundException("Directory does not exist or is not readable.");
            }
            jobs = jobList.stream().peek(o -> log.info("已加载任务：{}，任务详情：{}", o.getJobName(), o.toString())).collect(Collectors.toMap(OnmyojiJob::getJobId, r -> r));
        }
        log.info("==============任务加载完毕=============");
        return jobs;
    }

    /**
     * 任务检查
     * @param job
     */
    private void checkJob(OnmyojiJob job) {
        Assert.hasLength(job.getJobId(), "jobId不能为空");
        Assert.hasLength(job.getJobName(), "jobName不能为空");
        Assert.notNull(job.getTeamType(), "teamType不能为空");
        Assert.notNull(job.getHangUpType(), "hangUpType不能为空");
        Assert.hasLength(job.getProducerName(), "producerName不能为空");
        Assert.hasLength(job.getTerminal(), "terminal不能为空");
        Assert.notEmpty(job.getWindowNameList(), "窗口列表不能为空");
        Assert.notEmpty(job.getPictureList(), "任务截图不能为空");

        //检查项：组队类型、挂机类型、处理器、图片是否存在
        if (job.getTeamType().equals(TeamTypeEnum.SOLO)) {
            Assert.isTrue(job.getWindowNameList().size() == 1, "任务窗口数量非法");
        }
        if (!job.getTeamType().equals(TeamTypeEnum.SOLO) && !job.getTeamType().equals(TeamTypeEnum.TEAM_MATCHING)) {
            Assert.isTrue(job.getWindowNameList().size() > 1, "任务窗口数量非法");
        }

        if (job.getHangUpType().equals(HangUpTypeEnum.TIMES)) {
            Assert.notNull(job.getTimes(), "挂机次数不能为空");
        }
        if (job.getHangUpType().equals(HangUpTypeEnum.TIME_FROM_NOW)) {
            Assert.notNull(job.getTimes(), "挂机时长不能为空");
        }

    }

}
