package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.components.Matcher;
import com.commons.onmyoji.config.TanSuoConfig;
import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.enums.HangUpTypeEnum;
import com.commons.onmyoji.enums.TeamTypeEnum;
import com.commons.onmyoji.job.OnmyojiJob;
import com.commons.onmyoji.producer.InstanceZoneBaseProducer;
import com.commons.onmyoji.service.CommonService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Title: 探索挂机脚本处理器 - 默认困28，手动勾自动轮换，尽量单刷  组队很麻烦，效率也不如单刷
 *  另外：记得开轮换
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 22:39
 */
@Component("TanSuo")
@Slf4j
public class TanSuoProducer extends InstanceZoneBaseProducer<TanSuoConfig> {

    @Resource
    Matcher matcher;

    /**
     * 从哪个界面开始，庭院？还是御魂界面？ 或者兼容？
     * 1. 单刷 or 组队？
     * 2. 限时 or 限次 or 刷到死？
     * 容错机制：
     * 1. 宠物发现额外奖励
     * 2. 组队时队友超时，重新邀请队友
     *
     * @param job
     */
    @Override
    public void prepare(OnmyojiJob<TanSuoConfig> job) {

        // todo 清理结界突破（9退4）

        // 返回至庭院
        //commonService.backToYard(job.getTeamType());
        // 从庭院进入探索
        // 配置： 层数、截图存放位置
        TanSuoConfig jobConfig = job.getConfig();
        String imgDirectory = System.getProperty("user.dir") + "\\" + jobConfig.imgPath + "\\";

        String chapter28 = imgDirectory + "chapter28.png";
        String kunnan = imgDirectory + "dificulty-kunnan.png";
        String tansuo = imgDirectory + "tansuo.png";
        String i = imgDirectory + "img.png";
        String reward = imgDirectory + "reward.png";
        String end = imgDirectory + "end.png";

        Set<String> imgList = new HashSet<>();
        imgList.add(chapter28);
        imgList.add(kunnan);
        imgList.add(tansuo);
        imgList.add(i);
        imgList.add(reward);
        imgList.add(end);
        matcher.init(imgList);

    }

    @Override
    public String getProducerName() {
        return "探索";
    }
}
