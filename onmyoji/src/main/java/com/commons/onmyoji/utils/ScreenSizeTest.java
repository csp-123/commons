package com.commons.onmyoji.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class ScreenSizeTest {
    private static Logger logger = LoggerFactory.getLogger(ScreenSizeTest.class);

    public static void main(String[] args) {
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
        logger.info(String.format("显示器分辨率缩放比例，X：%s，Y：%s",uiScaleX,uiScaleY));
    }
}
