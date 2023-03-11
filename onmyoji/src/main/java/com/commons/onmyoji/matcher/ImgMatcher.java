package com.commons.onmyoji.matcher;

import com.alibaba.fastjson.JSON;
import com.commons.onmyoji.utils.FindRobot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static org.springframework.boot.autoconfigure.condition.ConditionOutcome.match;

/**
 * Description:
 * 图片匹配器
 * Author: chish
 * Date: 2023/3/12 1:07
 */
@Component
public class ImgMatcher {

    private static final Logger logger = LoggerFactory.getLogger(ImgMatcher.class);

    private static final Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 屏幕
     */
    private BufferedImage screen = getFullScreenShot();

    /**
     * 来源图片
     */
    private BufferedImage srcImg;

    /**
     * 目标图片
     */
    private BufferedImage targetImg;

    /**
     * 目标图宽度
     */
    private int targetImgWidth;

    /**
     * 目标图高度
     */
    private int targetImgHeight;

    /**
     * 来源图宽度
     */
    private int srcImgWidth;

    /**
     * 来源图高度
     */
    private int srcImgHeight;

    /**
     * 来源图RGB数据
     */
    private int[][] srcImgRGBData;

    /**
     * 目标图RGB数据
     */
    private int[][] targetImgRGBData;

    /**
     * 匹配结果
     */
    private List<MatchResult> results = new ArrayList<>();

    /**
     * 匹配并点击
     *
     * @param srcImgPath    来源图路径
     * @param targetImgPath 目标图路径
     * @param count         需要点击的数量
     * @param random        是否需要进行随机处理
     * @return 是否完成点击事件
     */
    public boolean matchAndClick(String srcImgPath, String targetImgPath, Integer count, boolean random) {
        // 重新加载匹配器
        reloadMatcher(srcImgPath, targetImgPath);
        // 循环匹配
        do {
            this.match();
        } while (results.size() != count);
        // 点击
        results.stream()
                // 移动鼠标并点击
                .forEach(matchResult -> {
                    mouseMove(matchResult.locationX, matchResult.locationY, random, targetImgWidth, targetImgHeight);
                    leftClick(300, true);
                });

        return true;
    }

    /**
     * 进行匹配
     */
    private void match() {
        if (!CollectionUtils.isEmpty(results)) {
            results.clear();
        }
        //根据目标图的尺寸，得到目标图四个角映射到屏幕截图上的四个点，
        //判断截图上对应的四个点与图B的四个角像素点的值是否相同，
        //如果相同就将屏幕截图上映射范围内的所有的点与目标图的所有的点进行比较。
        for (int y = 0; y < srcImgHeight - targetImgHeight; y++) {
            for (int x = 0; x < srcImgWidth - targetImgWidth; x++) {
                if ((targetImgRGBData[0][0] ^ srcImgRGBData[y][x]) == 0
                        && (targetImgRGBData[0][targetImgWidth - 1] ^ srcImgRGBData[y][x + targetImgWidth - 1]) == 0
                        && (targetImgRGBData[targetImgHeight - 1][targetImgWidth - 1] ^ srcImgRGBData[y + targetImgHeight - 1][x + targetImgWidth - 1]) == 0
                        && (targetImgRGBData[targetImgHeight - 1][0] ^ srcImgRGBData[y + targetImgHeight - 1][x]) == 0) {

                    //如果比较结果完全相同，则说明图片找到，填充查找到的位置坐标数据到查找结果数组。
                    boolean found = isMatchAll(y, x);

                    if (found) {
                        //y
                        int minY = y;
                        int maxY = y + targetImgHeight;
                        int locationY = ((minY + maxY) / 2);
                        //x
                        int minX = x;
                        int maxX = x + targetImgWidth;
                        int locationX = ((minX + maxX) / 2);
                        results.add(new MatchResult(locationX, locationY));
                    }
                }
            }
        }

        logger.info(String.format("匹配结果：%s", JSON.toJSONString(results)));
    }

    /**
     * 重新加载匹配器
     *
     * @param srcImgPath    来源图片路径
     * @param targetImgPath 目标图片路径
     */
    private void reloadMatcher(String srcImgPath, String targetImgPath) {
        srcImg = StringUtils.hasText(srcImgPath) ? getBfImageFromPath(srcImgPath) : screen;
        targetImg = getBfImageFromPath(targetImgPath);
        srcImgRGBData = this.getImageGRB(srcImg);
        targetImgRGBData = this.getImageGRB(targetImg);
        srcImgWidth = srcImg.getWidth();
        srcImgHeight = srcImg.getHeight();
        targetImgWidth = targetImg.getWidth();
        targetImgHeight = targetImg.getHeight();
    }

    /**
     * 全屏截图
     *
     * @return 返回BufferedImage
     */
    public BufferedImage getFullScreenShot() {
        BufferedImage bfImage = null;
        int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        try {
            Robot robot = new Robot();
            bfImage = robot.createScreenCapture(new Rectangle(0, 0, width, height));
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return bfImage;
    }

    /**
     * 从本地文件读取目标图片
     *
     * @param path - 图片绝对路径
     * @return 本地图片的BufferedImage对象
     */
    public BufferedImage getBfImageFromPath(String path) {
        BufferedImage bfImage = null;
        try {
            bfImage = ImageIO.read(new File(path));

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
    public int[][] getImageGRB(BufferedImage bfImage) {
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
     * 判断屏幕截图上目标图映射范围内的全部点是否全部和小图的点一一对应。
     *
     * @param y - 与目标图左上角像素点想匹配的屏幕截图y坐标
     * @param x - 与目标图左上角像素点想匹配的屏幕截图x坐标
     * @return
     */
    public boolean isMatchAll(int y, int x) {
        int biggerY = 0;
        int biggerX = 0;
        int xor = 0;
        for (int smallerY = 0; smallerY < targetImgHeight; smallerY++) {
            biggerY = y + smallerY;
            for (int smallerX = 0; smallerX < targetImgWidth; smallerX++) {
                biggerX = x + smallerX;
                if (biggerY >= srcImgHeight || biggerX >= srcImgWidth) {
                    return false;
                }
                xor = targetImgRGBData[smallerY][smallerX] ^ srcImgRGBData[biggerY][biggerX];
                if (xor != 0) {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * 单击鼠标左键
     */
    private void leftClick(int time, boolean random) {
        leftDown();
        int delayTime = setAutoDelay(time == 0 ? 100 : time, random);
        leftUp();
        logger.info(String.format("单机鼠标左键成功！是否随机延时：[%s]", random) + (random ? String.format("，随机延时：[%s]", delayTime) : ""));
    }

    /**
     * 鼠标移动
     * @param x x坐标
     * @param y y坐标
     * @param random 是否需要进行随机处理
     * @param targetImgWidth 图片宽度
     * @param targetImgHeight 图片高度
     */
    private void mouseMove(int x, int y, boolean random, int targetImgWidth, int targetImgHeight) {
        int trueX = random ? buildRandomLocation(x, targetImgWidth) : x;
        int trueY = random ? buildRandomLocation(y, targetImgHeight) : y;
        robot.mouseMove(trueX, trueY);
        logger.info(String.format("鼠标移动成功！指定移动位置：[%s][%s]，是否随机处理：[%s]，实际移动位置：[%s][%s]", x, y, random, trueX, trueY));
        // 鼠标移动后停顿一段时间，防止被检测
        waitSomeTime(200, 400);
    }

    /**
     * 左击按下
     */
    private void leftDown() {
        robot.mousePress(InputEvent.BUTTON1_MASK);
    }

    /**
     * 左击释放
     */
    private void leftUp() {
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    /**
     * 设置延时时间，毫秒
     *
     * @param time   延时
     * @param random 是否进行随机处理
     */
    private int setAutoDelay(int time, boolean random) {
        // 500 -> 250~500
        int delayTime = new Random().nextInt(time - time / 2 + 1) + time / 2;

        time = random ? delayTime : time;
        robot.setAutoDelay(time);

        return time;
    }

    /**
     * 等待一段时间
     * @param time1
     * @param time2
     */
    private void waitSomeTime(int time1, int time2) {
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
     *      例：（125，226） 64*58 横坐标：position = 125，横向长度： size = 64
     *      得出最终横坐标 125-(64/2)<result<125+(64/2) 即   93<result<157
     * @param position 像素单维度坐标
     * @param length 图像单维度长度
     * @return 最终坐标
     */
    private int buildRandomLocation(int position, int length) {
        Random random = new Random();
        int min = position - length / 2;
        int max = position + length / 2;
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * 匹配结果
     */
    class MatchResult {

        /**
         * x坐标
         */
        private Integer locationX;

        /**
         * Y坐标
         */
        private Integer locationY;

        MatchResult(Integer x, Integer y) {
            locationX = x;
            locationY = y;
        }

        public Integer getLocationX() {
            return locationX;
        }

        public void setLocationX(Integer locationX) {
            this.locationX = locationX;
        }

        public Integer getLocationY() {
            return locationY;
        }

        public void setLocationY(Integer locationY) {
            this.locationY = locationY;
        }
    }

}