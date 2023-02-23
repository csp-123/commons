package com.commons.onmyoji.producer.impl;

import com.commons.onmyoji.config.RoyalSoulConfig;
import com.commons.onmyoji.job.OnmyojiJob;
import com.commons.onmyoji.producer.InstanceZoneBaseProducer;
import org.springframework.stereotype.Component;

/**
 * Title: 御魂脚本处理器
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 22:39
 */
@Component("RoyalSoul")
public class RoyalSoulProducer extends InstanceZoneBaseProducer<RoyalSoulConfig> {

    @Override
    public void produce(OnmyojiJob<RoyalSoulConfig> job) {
        // 配置： 层数、截图存放位置
        RoyalSoulConfig jobConfig = job.getConfig();

        /*
            从哪个界面开始，庭院？还是御魂界面？ 或者兼容？
                1. 单刷 or 组队？
                2. 限时 or 限次 or 刷到死？
                3. 每次刷之前判断体力
                4. 刷完开关buff

         */


    }

    @Override
    public String getProcuderName() {
        return "御魂";
    }
}
