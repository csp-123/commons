package com.commons.onmyoji.utils;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.geom.AffineTransform;

public class ScreenSizeTest {
    private static Logger logger = LoggerFactory.getLogger(ScreenSizeTest.class);

    public static void main(String[] args) throws AWTException {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = defaultToolkit.getScreenSize();
        double width2 = screenSize.getWidth();
        double height2 = screenSize.getHeight();
        logger.info(String.format("显示器尺寸：%s x %s",width2,height2));
        
        GraphicsDevice graphDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode disMode = graphDevice.getDisplayMode();
        int width = disMode.getWidth();
        int height = disMode.getHeight();
        logger.info(String.format("显示器分辨率：%s x %s",width,height));

        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().
                getDefaultConfiguration();

        AffineTransform tx = gc.getDefaultTransform();
        double uiScaleX = tx.getScaleX();
        double uiScaleY = tx.getScaleY();
//        logger.info(String.format("显示器分辨率缩放比例，X：%s，Y：%s",uiScaleX,uiScaleY));


        // 记录游戏窗口位置、大小
        User32 user32 = User32.INSTANCE;
        // 念山行  今时月
        WinDef.HWND hwnd2 = user32.FindWindow(null, "念山行");
        Assert.notNull(hwnd2, String.format("找不到窗口：%s", "念山行"));
        // 获取窗口大小
        WinDef.RECT rect = new WinDef.RECT();
        user32.GetWindowRect(hwnd2, rect);
        logger.info(String.format("窗口位置： left: %s, top: %s", rect.left, rect.top));
        logger.info(String.format("窗口位置： right: %s, bottom: %s", rect.right, rect.bottom));
        Robot robot = new Robot();

        // 计算宽度和高度的缩放比
        double widthScale = (double) width / width2;
        double heightScale = (double)height / height2;
        // 选择更大的缩放比作为最终的缩放比
        double scale = Math.max(widthScale, heightScale);
        logger.info("scale: {}", scale);
        //切记！！！！！！！！！！    这里须先将坐标设置为-1
        robot.mouseMove(-1,-1);
        int javaX = (int) (rect.right / scale);
        int javaY = (int) (rect.bottom / scale);
        robot.mouseMove(javaX,javaY);
//        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

//    public int getScreenScalingFactor() {
//        // 获取图形环境
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        // 获取当前的图形设备，通常是主显示器
//        GraphicsDevice gd = ge.getDefaultScreenDevice();
//        // 获取屏幕分辨率
//        int screenWidth = gd.getDisplayMode().getWidth();
//        int screenHeight = gd.getDisplayMode().getHeight();
//        // 获取默认工具包
//        Toolkit toolkit = Toolkit.getDefaultToolkit();
//        // 获取屏幕尺寸（虚拟尺寸，非物理尺寸）
//        Dimension screenSize = toolkit.getScreenSize();
//        // 输出屏幕宽度和高度
//        int Width = screenSize.width;
//        int Height = screenSize.height;
//        final int TARGET_WIDTH = Width;
//        final int TARGET_HEIGHT = Height;
//        // 计算宽度和高度的缩放比
//        double widthScale = (double)screenWidth / TARGET_WIDTH;
//        double heightScale = (double)screenHeight / TARGET_HEIGHT;
//        // 选择更大的缩放比作为最终的缩放比
//        double scale = Math.max(widthScale, heightScale);
//        return (int) scale;
//    }
//
//    public void recordMouseClick(int x, int y, int scale) {
//        int javaX = x / scale;
//        int javaY = y / scale;
//        //切记！！！！！！！！！！    这里须先将坐标设置为-1
//        robot.mouseMove(-1,-1);
//        robot.mouseMove(javaX,javaY);
//        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
//    }
}
