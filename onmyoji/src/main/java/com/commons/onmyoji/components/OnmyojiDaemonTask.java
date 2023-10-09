package com.commons.onmyoji.components;

import com.commons.onmyoji.config.OnmyojiConfig;
import com.commons.onmyoji.constant.OnmyojiConstant;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Collections;
import java.util.TimerTask;

/**
 * @description: 守护线程 - 处理异常事件
 * @author: cccsp
 * @date: 2023/3/15 13:43
 */
@Component
public class OnmyojiDaemonTask extends TimerTask {

    @SneakyThrows
    @Override
    public void run() {
        Matcher matcher = new Matcher();
        OnmyojiConfig onmyojiConfig = new OnmyojiConfig();
        onmyojiConfig.setThreshold(0.95);
        matcher.setOnmyojiConfig(onmyojiConfig);
        matcher.setRobot(new Robot());
        String imgDirectory = System.getProperty("user.dir") + "\\" + OnmyojiConstant.COMMON_IMG_PATH + OnmyojiConstant.OFFER_REWARD;
        matcher.init(Collections.singleton(imgDirectory));
        matcher.matchOneImgRU(imgDirectory, false);

    }
}
