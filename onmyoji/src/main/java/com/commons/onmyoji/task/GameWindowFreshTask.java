package com.commons.onmyoji.task;

import com.commons.onmyoji.entity.ScreenSnapshot;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

/**
 * 游戏窗口刷新：实时监测游戏窗口位置、窗口大小
 * @author chishupeng
 * @date 2023/8/18 2:45 PM
 */
@Component
public class GameWindowFreshTask extends TimerTask {
    @Override
    public void run() {
        // todo 记录游戏窗口位置、大小
        ScreenSnapshot instance = ScreenSnapshot.getInstance();
        instance.setX(0)
                .setY(0)
                .setWindowWidth(2560)
                .setWindowHeight(1440);
    }
}
