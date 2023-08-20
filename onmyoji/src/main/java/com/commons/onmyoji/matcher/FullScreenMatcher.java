package com.commons.onmyoji.matcher;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * 全屏匹配器
 * Author: chish
 * Date: 2023/3/12 1:07
 */
@Getter
public class FullScreenMatcher implements KeyListener {

    private ExecutorService executorService = new ThreadPoolExecutor(3, 10, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10));

    private static final Logger logger = LoggerFactory.getLogger(FullScreenMatcher.class);


    private static final Robot robot = getRobot();

    // 近似匹配阈值
    private static final double SIMILAR_THRESHOLD = 0.9D;

    /**
     * RGB数据缓存
     */
    private static Map<String, int[][]> RGBDataMap = new HashMap<>(16);


    /**
     * 全屏数据
     */
    private int[][] srcImgRGBData;

    /**
     * 全屏高
     */
    private final int fullScreenHeight;

    /**
     * 全屏宽
     */
    private final int fullScreenWidth;

    /**
     * 待匹配图片列表
     */
    private List<String> targetImgPathList;


    public FullScreenMatcher(List<String> targetImgPathList) {
        GraphicsDevice graphDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode disMode = graphDevice.getDisplayMode();
        int width = disMode.getWidth();
        int height = disMode.getHeight();
        this.fullScreenWidth = width;
        this.fullScreenHeight = height;
        this.load(targetImgPathList);
    }

    /**
     * 开始监控，符合即点击
     *
     * @param isSimilar 是否近似匹配
     */
    public void startWatching(boolean isSimilar) {
        // 多线程匹配
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                matchAndClick();
            }
        });
    }

    /**
     * 匹配并点击
     */
    private void matchAndClick() {
        while (true) {
            for (int x = 0; x < srcImgRGBData.length; x++) {
                for (int y = 0; y < srcImgRGBData[x].length; y++) {
                    for (int[][] value : RGBDataMap.values()) {
                        int col = value[0].length;
                        int row = value.length;
                        if ((value[0][0] ^ srcImgRGBData[x][y] ) == 0
                                && (value[0][col - 1] ^ srcImgRGBData[x][y + col - 1]) == 0
                                && (value[row - 1][col - 1] ^ srcImgRGBData[x + row - 1][y + col - 1]) == 0
                                && (value[row - 1][0] ^ srcImgRGBData[x + col - 1][y]) == 0) {
                            mouseMove((y+col)/2, (x+row)/2, true, col, row, true);
                            leftClick(300, true);
                        }
                    }
                }
            }
            reloadScreen();
        }
    }





    /**
     * 鼠标左键单击
     *
     * @param time   按下持续时间
     * @param random 按下持续时间是否进行随机处理
     */
    private static void leftClick(Integer time, boolean random) {
        // 默认300
        if (time == null)
            time = 300;
        leftDown();
        int delayTime = setAutoDelay(time == 0 ? 100 : time, random);
        leftUp();
        logger.info(String.format("单机鼠标左键成功！是否随机延时：[%s]", random) + (random ? String.format("，随机延时：[%s]", delayTime) : ""));
    }

    /**
     * 左击按下
     */
    private static void leftDown() {
        robot.mousePress(InputEvent.BUTTON1_MASK);
    }

    /**
     * 左击释放
     */
    private static void leftUp() {
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    /**
     * 设置延时时间，毫秒
     *
     * @param time   延时
     * @param random 是否进行随机处理
     */
    private static int setAutoDelay(int time, boolean random) {
        // 500 -> 250~500
        int delayTime = new Random().nextInt(time - time / 2 + 1) + time / 2;

        time = random ? delayTime : time;
        robot.setAutoDelay(time);

        return time;
    }


    /**
     * 鼠标移动
     *
     * @param x               x坐标
     * @param y               y坐标
     * @param random          是否需要进行随机处理
     * @param targetImgWidth  图片宽度
     * @param targetImgHeight 图片高度
     * @param delay           是否需要延时
     */
    private static void mouseMove(int x, int y, boolean random, int targetImgWidth, int targetImgHeight, boolean delay) {
        int trueX = random ? buildRandomLocation(x, targetImgWidth) : x;
        int trueY = random ? buildRandomLocation(y, targetImgHeight) : y;
        robot.mouseMove(trueX, trueY);
        logger.info(String.format("鼠标移动成功！指定移动位置：[%s][%s]，是否随机处理：[%s]，实际移动位置：[%s][%s]", x, y, random, trueX, trueY));
        // 鼠标移动后停顿一段时间，防止被检测
        if (delay)
            waitSomeTime(200, 400);
    }

    /**
     * 等待一段时间
     *
     * @param time1
     * @param time2
     */
    private static void waitSomeTime(int time1, int time2) {
        Random random = new Random();
        int max = Math.max(time1, time2);
        int min = Math.max(time1, time2);
        int randomTime = random.nextInt(max - min + 1) + min;

        try {
            Thread.sleep(randomTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 以中心点为构建随机位置
     * 例：（125，226） 64*58 横坐标：position = 125，横向长度： size = 64
     * 得出最终横坐标 125-(64/2)<result<125+(64/2) 即   93<result<157
     *
     * @param position 像素单维度坐标
     * @param length   图像单维度长度
     * @return 最终坐标
     */
    private static int buildRandomLocation(int position, int length) {
        Random random = new Random();
        int min = position - length / 2;
        int max = position + length / 2;
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * 初始化
     */
    private void load(List<String> targetImgPathList) {
        reloadScreen();
        targetImgPathList
                .forEach(targetImgPath -> {
                    BufferedImage bfImageFromPath = getBfImageFromPath(targetImgPath);
                    RGBDataMap.put(targetImgPath, getImageRGB(bfImageFromPath));
                });
    }


    /**
     * 从本地文件读取目标图片
     *
     * @param keyImagePath - 图片绝对路径
     * @return 本地图片的BufferedImage对象
     */
    private BufferedImage getBfImageFromPath(String keyImagePath) {
        BufferedImage bfImage = null;
        try {
            bfImage = ImageIO.read(new File(keyImagePath));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bfImage;
    }

    /**
     * 根据BufferedImage获取图片RGB数组
     *
     * @param bfImage
     * @return
     */
    private static int[][] getImageRGB(BufferedImage bfImage) {
        int width = bfImage.getWidth();
        int height = bfImage.getHeight();
        int[][] result = new int[height][width];
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                //使用getRGB(w, h)获取该点的颜色值是ARGB，而在实际应用中使用的是RGB，所以需要将ARGB转化成RGB，即bufImg.getRGB(w, h) & 0xFFFFFF。
                result[h][w] = bfImage.getRGB(w, h) & 0xFFFFFF;
            }
        }
        return result;
    }

    /**
     * 重新加载屏幕
     */
    private void reloadScreen() {

        BufferedImage bufferedImage = getFullScreenShot();

        this.srcImgRGBData = getImageRGB(bufferedImage);

    }

    /**
     * 全屏截图
     *
     * @return 返回BufferedImage
     */
    private BufferedImage getFullScreenShot() {
        BufferedImage bfImage;
        bfImage = robot.createScreenCapture(new Rectangle(0, 0, this.fullScreenWidth, this.fullScreenHeight));
        return bfImage;
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            executorService.shutdown();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}