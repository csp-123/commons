package com.commons.onmyoji.matcher;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.util.CollectionUtils;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


/**
 * Description:
 * 图片匹配器
 * Author: chish
 * Date: 2023/3/12 1:07
 */
public class ImgMatcher {

    public static final double scale = 1.5D;

    private static final Logger logger = LoggerFactory.getLogger(ImgMatcher.class);

    private static final Robot robot = getRobot();

    /**
     * 匹配并点击
     *
     * @param srcImgPath    来源图路径
     * @param targetImgPath 目标图路径
     * @param count         需要点击的数量
     * @param random        是否需要进行随机处理
     * @return 是否完成点击事件
     */
    public static void matchAndClick(String srcImgPath, String targetImgPath, Integer count, boolean random) {
        // 循环匹配
        do {
            // 重新加载匹配器
            reloadMatcher(srcImgPath, targetImgPath);
            match();
        } while (results.size() != count);
        // 点击
        results.stream()
                // 移动鼠标并点击
                .forEach(matchResult -> {
                    mouseMove(matchResult.locationX, matchResult.locationY, random, targetImgWidth, targetImgHeight);
                    leftClick(300, true);
                });


    }

    /**
     * 匹配并点击
     *
     * @param srcBfImg    来源图BufferedImage
     * @param targetImgPath 目标图路径
     * @param count         需要点击的数量
     * @param random        是否需要进行随机处理
     * @return 是否完成点击事件
     */
    public static void matchAndClick(BufferedImage srcBfImg, String targetImgPath, Integer count, boolean random) {
        // 循环匹配
        do {
            // 重新加载匹配器
            reloadMatcher(srcBfImg, targetImgPath);
            match();
        } while (results.size() != count);
        // 点击
        results.stream()
                // 移动鼠标并点击
                .forEach(matchResult -> {
                    mouseMove(matchResult.locationX, matchResult.locationY, random, targetImgWidth, targetImgHeight);
                    leftClick(300, true);
                });


    }

    /**
     * 匹配并点击
     *
     * @param targetImgPath 目标图路径
     * @param count         需要点击的数量
     * @param random        是否需要进行随机处理
     * @return 是否完成点击事件
     */
    public static void matchAndClick(String targetImgPath, Integer count, boolean random) {
        // 循环匹配
        do {
            // 重新加载匹配器
            reloadMatcher(targetImgPath);
            match();
        } while (results.size() != count);
        // 点击
        results.stream()
                // 移动鼠标并点击
                .forEach(matchResult -> {
                    mouseMove(matchResult.locationX, matchResult.locationY, random, targetImgWidth, targetImgHeight);
                    leftClick(300, true);
                    waitSomeTime(200, 400);
                });


    }


    /**
     * 匹配并点击第一个
     *
     * @param targetImgPath 目标图路径
     * @param random        是否需要进行随机处理
     * @return 是否完成点击事件
     */
    public static void matchAndClickFirst(String targetImgPath, boolean random) {
        // 循环匹配
        do {
            // 重新加载匹配器
            reloadMatcher(targetImgPath);
            match();
        } while (results.size() == 0);
        // 点击
        MatchResult result = results.get(0);
        mouseMove(result.locationX, result.locationY, random, targetImgWidth, targetImgHeight);
        leftClick(300, true);

    }

    /**
     * 是否匹配到目标图片
     * @param srcImgPath 源图片
     * @param targetImgPath 目标图片
     * @param count 预期匹配次数
     * @return true or false
     */
    public static boolean match(String srcImgPath, String targetImgPath, Integer count) {
        reloadMatcher(srcImgPath, targetImgPath);
        match();
        return results.size() == count;
    }

    /**
     * 是否匹配到目标图片
     * @param bfImg 源图片
     * @param targetImgPath 目标图片
     * @param count 预期匹配次数
     * @return true or false
     */
    public static boolean match(BufferedImage bfImg, String targetImgPath, Integer count) {
        reloadMatcher(bfImg, targetImgPath);
        match();
        return results.size() == count;
    }

    /**
     * 是否匹配到目标图片
     * @param targetImgPath 目标图片
     * @param count 预期匹配次数
     * @return true or false
     */
    public static boolean match(String targetImgPath, Integer count) {
        reloadMatcher(targetImgPath);
        match();
        return results.size() == count;
    }

    /**
     * 是否近似匹配到目标图片
     * @param targetImgPath 目标图片
     * @param count 预期匹配次数
     * @return true or false
     */
    public static boolean similarMatch(String targetImgPath, Integer count) {
        reloadMatcher(targetImgPath);
        similarMatch();
        return results.size() == count;
    }

    /**
     * 近似匹配并点击第一个
     *
     * @param targetImgPath 目标图路径
     * @param random        是否需要进行随机处理
     * @return 是否完成点击事件
     */
    public static boolean similarMatchAndClickFirst(String targetImgPath, boolean random) {
        // 循环匹配
        do {
            // 重新加载匹配器
            reloadMatcher(targetImgPath);
            similarMatch();
        } while (results.size() == 0);
        // 点击
        MatchResult result = results.get(0);
        mouseMoveNoDelay(result.locationX, result.locationY, random, targetImgWidth, targetImgHeight);
        leftClick(100, true);
        return true;

    }


    /**
     * 匹配目标图片结果
     * @param targetImgPath 目标图片
     * @return count
     */
    public static int count(String targetImgPath) {
        reloadMatcher(targetImgPath);
        match();
        return results.size();
    }

    /**
     * 全屏截图
     * @return 返回BufferedImage
     */
    public static BufferedImage getFullScreenShot() {
        BufferedImage bfImage = null;
        Dimension screenSize = getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        try {
            Robot robot = new Robot();
            bfImage = robot.createScreenCapture(new Rectangle(0, 0, width, height));
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return bfImage;
    }


    /**
     * 点击目标图右上角
     * @param imgDirectory
     */
    public static void ClickImgRU(String imgDirectory) {
        // 循环匹配
        do {
            // 重新加载匹配器
            reloadMatcher(imgDirectory);
            match();
        } while (results.isEmpty());
        // 点击
        results.stream()
                // 移动鼠标并点击
                .forEach(matchResult -> {
                    mouseMove(matchResult.locationX + targetImgWidth/2, matchResult.locationY - targetImgHeight/2, false, targetImgWidth, targetImgHeight);
                    leftClick(300, true);
                });
    }


    /**
     * 获取匹配结果
     * @return
     */
    public static List<MatchResult> getResult() {
        return results;
    }


    /**
     * 匹配目标图片结果
     * @param targetImgPath 目标图片
     * @return count
     */
    public static int similarCount(String targetImgPath) {
        reloadMatcher(targetImgPath);
        similarMatch();
        return results.size();
    }

    // ====================================================================================================

    private static String targetImgPath;

    /**
     * 来源图片
     */
    private static BufferedImage srcImg;

    /**
     * 目标图片
     */
    private static BufferedImage targetImg;

    /**
     * 目标图宽度
     */
    private static int targetImgWidth;

    /**
     * 目标图高度
     */
    private static int targetImgHeight;

    /**
     * 来源图宽度
     */
    private static int srcImgWidth;

    /**
     * 来源图高度
     */
    private static int srcImgHeight;

    /**
     * 来源图RGB数据
     */
    private static int[][] srcImgRGBData;

    /**
     * 目标图RGB数据
     */
    private static int[][] targetImgRGBData;

    /**
     * 匹配结果
     */
    private static List<MatchResult> results = new ArrayList<>();


    /**
     * 进行匹配
     */
    private static void match() {
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
                        logger.info("found one！");
                        //y
                        double v = y / scale;
                        long round = Math.round(v);
                        int minY = (int) round;
                        int maxY = minY + targetImgHeight;
                        int locationY = ((minY + maxY) / 2);
                        //x
                        double v1 = x / scale;
                        long round1 = Math.round(v1);
                        int minX = (int) round1;
                        int maxX = minX + targetImgWidth;
                        int locationX = ((minX + maxX) / 2);
                        results.add(new MatchResult(locationX, locationY));
                    }
                }
            }
        }

        logger.info(String.format("匹配图片：%s，结果：%s", targetImgPath, JSON.toJSONString(results)));
    }


    /**
     * 近似匹配
     */
    private static void similarMatch() {
        if (!CollectionUtils.isEmpty(results)) {
            results.clear();
        }
        // 近似匹配 匹配中心点及其上下左右一像素点 相似度超60%即认定为匹配
        for (int y = 0; y < srcImgHeight; y++) {
            for (int x = 0; x < srcImgWidth; x++) {
                if ((srcImgRGBData[y][x] ^ targetImgRGBData[targetImgHeight/2][targetImgWidth/2]) == 0) {
                    int matchCount = 1;
                    // 上
                    if ((srcImgRGBData[y-1][x] ^ targetImgRGBData[targetImgHeight/2 - 1][targetImgWidth/2]) == 0) {
                        matchCount++;
                    }
                    // 下
                    if ((srcImgRGBData[y+1][x] ^ targetImgRGBData[targetImgHeight/2 + 1][targetImgWidth/2]) == 0) {
                        matchCount++;
                    }
                    // 左
                    if ((srcImgRGBData[y][x-1] ^ targetImgRGBData[targetImgHeight/2][targetImgWidth/2 - 1]) == 0) {
                        matchCount++;
                    }
                    // 右
                    if ((srcImgRGBData[y][x+1] ^ targetImgRGBData[targetImgHeight/2][targetImgWidth/2 + 1]) == 0) {
                        matchCount++;
                    }
                    if (matchCount >= 3) {
                        logger.info("found one！");
                        results.add(new MatchResult(x, y));
                    }

                }
            }
        }

        logger.info(String.format("近似匹配图片：%s，结果：%s", targetImgPath, JSON.toJSONString(results)));

    }


    /**
     * 重新加载匹配器
     *
     * @param targetImagePath 目标图片路径
     */
    private static void reloadMatcher(String targetImagePath) {
        srcImg = getFullScreenShot();
        targetImg = getBfImageFromPath(targetImagePath);
        srcImgRGBData = getImageRGB(srcImg);
        targetImgRGBData = getImageRGB(targetImg);
        srcImgWidth = srcImg.getWidth();
        srcImgHeight = srcImg.getHeight();
        targetImgWidth = targetImg.getWidth();
        targetImgHeight = targetImg.getHeight();
        targetImgPath = targetImagePath;
    }

    /**
     * 重新加载匹配器
     *
     * @param srcImgPath    来源图片路径
     * @param targetImgPath 目标图片路径
     */
    private static void reloadMatcher(String srcImgPath, String targetImgPath) {
        srcImg = getBfImageFromPath(srcImgPath);
        targetImg = getBfImageFromPath(targetImgPath);
        srcImgRGBData = getImageRGB(srcImg);
        targetImgRGBData = getImageRGB(targetImg);
        srcImgWidth = srcImg.getWidth();
        srcImgHeight = srcImg.getHeight();
        targetImgWidth = targetImg.getWidth();
        targetImgHeight = targetImg.getHeight();
    }

    /**
     * 重新加载匹配器
     *
     * @param srcBfImg    来源图片BufferedImage
     * @param targetImgPath 目标图片路径
     */
    private static void reloadMatcher(BufferedImage srcBfImg, String targetImgPath) {
        srcImg = srcBfImg;
        targetImg = getBfImageFromPath(targetImgPath);
        srcImgRGBData = getImageRGB(srcImg);
        targetImgRGBData = getImageRGB(targetImg);
        srcImgWidth = srcImg.getWidth();
        srcImgHeight = srcImg.getHeight();
        targetImgWidth = targetImg.getWidth();
        targetImgHeight = targetImg.getHeight();
    }

//    /**
//     * 全屏截图
//     *
//     * @return 返回BufferedImage
//     */
//    public static BufferedImage getFullScreenShot() {
//        BufferedImage bfImage = null;
//        int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
//        int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
//        try {
//            Robot robot = new Robot();
//            bfImage = robot.createScreenCapture(new Rectangle(0, 0, width, height));
//        } catch (AWTException e) {
//            e.printStackTrace();
//        }
//        return bfImage;
//    }


    /**
     * 从本地文件读取目标图片
     *
     * @param path - 图片绝对路径
     * @return 本地图片的BufferedImage对象
     */
    private static BufferedImage getBfImageFromPath(String path) {
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
     * 判断屏幕截图上目标图映射范围内的全部点是否全部和小图的点一一对应。
     *
     * @param y - 与目标图左上角像素点想匹配的屏幕截图y坐标
     * @param x - 与目标图左上角像素点想匹配的屏幕截图x坐标
     * @return
     */
    private static boolean isMatchAll(int y, int x) {
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
    private static void leftClick(int time, boolean random) {
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
    private static void mouseMove(int x, int y, boolean random, int targetImgWidth, int targetImgHeight) {
        int trueX = random ? buildRandomLocation(x, targetImgWidth) : x;
        int trueY = random ? buildRandomLocation(y, targetImgHeight) : y;
        robot.mouseMove(trueX, trueY);
        logger.info(String.format("鼠标移动成功！指定移动位置：[%s][%s]，是否随机处理：[%s]，实际移动位置：[%s][%s]", x, y, random, trueX, trueY));
        // 鼠标移动后停顿一段时间，防止被检测
        waitSomeTime(200, 400);
    }

    /**
     * 鼠标移动
     * @param x x坐标
     * @param y y坐标
     * @param random 是否需要进行随机处理
     * @param targetImgWidth 图片宽度
     * @param targetImgHeight 图片高度
     */
    private static void mouseMoveNoDelay(int x, int y, boolean random, int targetImgWidth, int targetImgHeight) {
        int trueX = random ? buildRandomLocation(x, targetImgWidth) : x;
        int trueY = random ? buildRandomLocation(y, targetImgHeight) : y;
        robot.mouseMove(trueX, trueY);
        logger.info(String.format("鼠标移动成功！指定移动位置：[%s][%s]，是否随机处理：[%s]，实际移动位置：[%s][%s]", x, y, random, trueX, trueY));
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
     * 等待一段时间
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
     *      例：（125，226） 64*58 横坐标：position = 125，横向长度： size = 64
     *      得出最终横坐标 125-(64/2)<result<125+(64/2) 即   93<result<157
     * @param position 像素单维度坐标
     * @param length 图像单维度长度
     * @return 最终坐标
     */
    private static int buildRandomLocation(int position, int length) {
        Random random = new Random();
        int min = position - length / 2;
        int max = position + length / 2;
        return random.nextInt(max - min + 1) + min;
    }

    private static Robot getRobot() {
        try {
            return new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 点击
     * @param x x坐标
     * @param y y坐标
     * @param width 范围宽带
     * @param length 范围高度
     * @param random 是否随机处理
     */
    public static void click(int x, int y, int width, int length, boolean random) {
        mouseMove(x, y, random, width, length);
        leftClick(300, true);
        waitSomeTime(200, 400);
    }


    /**
     * 匹配结果静态内部类
     */
    public static class MatchResult {

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



    public static BufferedImage getScreenImage(int x, int y, int width, int height) {
        try {
            return (new Robot()).createScreenCapture(new Rectangle(x, y, width, height));
        } catch (Exception e) {
            return null;
        }
    }

    public static Dimension getScreenSize() {
        return getScreenSizeFromImage(getScreenImage(0, 0, 10000, 5000));
    }

    private static Dimension getScreenSizeFromImage(BufferedImage image) {
        if (image == null) {
            return Toolkit.getDefaultToolkit().getScreenSize();
        }
        int width = image.getWidth();
        int height = image.getHeight();
        long[] w = new long[width];
        long[] h = new long[height];
        for (int i = 0; i < width; i++) {
            long value = 0;
            for (int j = 0; j < height; j++) {
                value += image.getRGB(i, j);
            }
            w[i] = value;
        }
        for (int i = 0; i < height; i++) {
            long value = 0;
            for (int j = 0; j < width; j++) {
                value += image.getRGB(j, i);
            }
            h[i] = value;
        }
        return new Dimension(getValidSize(w), getValidSize(h));
    }

    private static int getValidSize(long[] v) {
        if (v == null || v.length == 0) {
            return 0;
        }
        long last = v[v.length - 1];
        for (int i = v.length - 1; i >= 0; i--) {
            if (last != v[i]) {
                return i + 1;
            }
        }
        return 0;
    }

}