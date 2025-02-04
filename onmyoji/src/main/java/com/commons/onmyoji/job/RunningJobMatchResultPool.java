package com.commons.onmyoji.job;

import com.commons.onmyoji.entity.MatchResult;
import com.commons.onmyoji.entity.OnmyojiJob;
import com.commons.onmyoji.entity.TargetMatchingResult;
import com.google.protobuf.ServiceException;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

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
public class RunningJobMatchResultPool {


    /**
     * 任务匹配结果map
     * k：jobId
     * v：MatchResult
     */
    Map<String, MatchResult> matchResultMap;

    @PostConstruct
    public void init(){
        matchResultMap = new HashMap<>();
    }


    @SneakyThrows
    public void add(OnmyojiJob job) {
        MatchResult value = new MatchResult();
        HashMap<String, Set<TargetMatchingResult>> resultItemMap = new HashMap<>();

        for (String windowName : job.getWindowNameList()) {
            File parentPath = new File(job.getImgDirectory());
            File[] files = parentPath.listFiles();
            if (files == null || files.length == 0) {
                log.error("目录下无文件");
                throw new ServiceException("目录下无文件");
            }
            List<String> imgPaths = Arrays.stream(files).map(File::getAbsolutePath).collect(Collectors.toList());
            Set<TargetMatchingResult> targetMatchingResults = imgPaths.stream().map(o -> {
                TargetMatchingResult targetMatchingResult = new TargetMatchingResult();
                targetMatchingResult.setTargetImgName(getNameFromPath(o));
                targetMatchingResult.setWindowName(windowName);
                return targetMatchingResult;
            }).collect(Collectors.toSet());
            resultItemMap.put(windowName, targetMatchingResults);
        }
        value.setResultItemMap(resultItemMap);
        matchResultMap.put(job.getJobId(), value);
    }

    public void remove(String jobId) {
        matchResultMap.remove(jobId);
    }


    public void removeAll() {
        matchResultMap.clear();
    }

    public MatchResult get(String jobId) {
        return matchResultMap.get(jobId);
    }

    public Boolean contains(String jobId) {
        return matchResultMap.containsKey(jobId);
    }


    /**
     * 从图片路径中提取图片名称
     *
     * @param path
     * @return
     */
    private String getNameFromPath(String path) {
        Assert.hasText(path, "图片路径为空");
        try {
            String[] dotSplits = path.split("\\.");
            String pre = dotSplits[dotSplits.length - 2];
            String[] split = pre.split("\\\\");
            return split[split.length - 1];
        } catch (Exception e) {
            log.error("图片名称提取失败");
            throw new UnknownFormatConversionException(e.getMessage());
        }
    }

}
