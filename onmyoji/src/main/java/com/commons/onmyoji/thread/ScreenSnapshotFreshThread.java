package com.commons.onmyoji.thread;

import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.entity.ScreenSnapshot;
import com.commons.onmyoji.matcher.ImgMatcher2;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @description: 屏幕刷新现场
 * @author: cccsp
 * @date: 2023/3/15 13:43
 */
public class ScreenSnapshotFreshThread extends Thread {

    @Override
    public void run() {
        while (true) {
            Robot robot = getRobot();
            GraphicsDevice graphDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            DisplayMode disMode = graphDevice.getDisplayMode();
            BufferedImage bfImage;
            bfImage = robot.createScreenCapture(new Rectangle(0, 0, disMode.getWidth(), disMode.getHeight()));
            ScreenSnapshot.getInstance().setBufferedImage(bfImage);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 获取robot对象
     *
     * @return Robot
     */
    private static Robot getRobot() {
        try {
            return new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
}
