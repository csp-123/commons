package com.commons.onmyoji.utils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * @description: cod感染模式挂机
 * @author: cccsp
 * @date: 2023/2/26 23:51
 */
public class CodHangUp {

    public static void main(String[] args) throws AWTException {
        Robot robot = new Robot();

        while (true) {
            robot.delay(5000);
            robot.keyPress(KeyEvent.VK_W);
            robot.delay(1000);
            robot.keyRelease(KeyEvent.VK_W);
            robot.delay(5000);
            robot.keyPress(KeyEvent.VK_A);
            robot.delay(1000);
            robot.keyRelease(KeyEvent.VK_A);
            robot.delay(5000);
            robot.keyPress(KeyEvent.VK_S);
            robot.delay(1000);
            robot.keyRelease(KeyEvent.VK_S);
            robot.delay(5000);
            robot.keyPress(KeyEvent.VK_D);
            robot.delay(1000);
            robot.keyRelease(KeyEvent.VK_D);


        }
    }
}
