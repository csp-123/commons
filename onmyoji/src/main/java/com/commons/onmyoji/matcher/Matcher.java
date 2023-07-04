package com.commons.onmyoji.matcher;

import com.alibaba.fastjson.JSON;
import com.commons.onmyoji.utils.ImageSimilarity;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Description:
 * 图片匹配器
 * Author: chish
 * Date: 2023/3/12 1:07
 */
@Getter
public class Matcher {

    private static final Logger logger = LoggerFactory.getLogger(Matcher.class);


    private static final Robot robot = getRobot();

    // 近似匹配阈值
    private static final double SIMILAR_THRESHOLD = 0.9D;

    /**
     * RGB数据缓存
     */
    private static Map<String, int[][]> RGBDataMap = new HashMap<>(16);

    /**
     * 近似匹配RBG数据缓存
     */
    private static Map<String, int[][]> similarRGBDataMap = new HashMap<>(16);

    /**
     * BufferedImage 数据缓存
     */
    private static Map<String, BufferedImage> imgMap = new HashMap<>(16);

    /**
     * 全屏高
     */
    private final int fullScreenHeight;

    /**
     * 全屏宽
     */
    private final int fullScreenWidth;

    /**
     * 来源图片路径
     */
    private String targetImgPath;

    /**
     * 来源图片路径
     */
    private String srcImgPath;

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
    private List<Matcher.MatchResult> results = new ArrayList<>();


    public Matcher(String targetImgPath, String srcImgPath, int screenHeight, int screenWidth) {
        this.targetImgPath = targetImgPath;
        this.srcImgPath = srcImgPath;
        this.fullScreenHeight = screenHeight;
        this.fullScreenWidth = screenWidth;

        this.reload(targetImgPath);
    }

    public Matcher(int screenWidth, int screenHeight) {
        this.fullScreenHeight = screenHeight;
        this.fullScreenWidth = screenWidth;

    }


    /**
     * 匹配数量
     * @param targetImgPath 目标图路径
     * @param isSimilar 是否是近似匹配
     * @return 数量
     */
    public int count(String targetImgPath, boolean isSimilar) {

        match(targetImgPath, isSimilar);
        return results.size();
    }

    /**
     * 点击所有匹配点
     * @param targetImgPath 目标图片
     * @param random 是否随机处理
     */
    public void click(String targetImgPath, boolean random, boolean isSimilar) {
        match(targetImgPath, isSimilar);

        // 点击
        results.stream()
                // 移动鼠标并点击
                .forEach(matchResult -> {
                    mouseMove(matchResult.locationX, matchResult.locationY, random, targetImgWidth, targetImgHeight, true);
                    leftClick(300, true);
                });
    }

    /**
     * 点击指定位置
     * @param x x坐标
     * @param y y坐标
     * @param targetImgPath 目标图路径，为空则代表精确点击，不需随机处理
     */

    public void click(Integer x, Integer y, String targetImgPath) {
        Integer height = 0;
        Integer width = 0;
        boolean random = StringUtils.hasText(targetImgPath);
        if (random) {
            BufferedImage bfImageFromPath = getBfImageFromPath(targetImgPath);
            height = bfImageFromPath.getHeight();
            width = bfImageFromPath.getWidth();
        }

        // 移动
        mouseMove(x, y, random, width, height, true);
        // 点击
        leftClick(0, false);


    }

    /**
     * 点击一个匹配点
     * @param targetImgPath 目标图路径
     * @param random 是否随机处理
     * @param isSimilar 是否近似匹配
     */
    public void clickFirst(String targetImgPath, boolean random, boolean isSimilar) {
        match(targetImgPath, isSimilar);
        // 点击
        if (results.isEmpty()) {
            return;
        }
        MatchResult matchResult = results.get(0);
        mouseMove(matchResult.locationX, matchResult.locationY, random, targetImgWidth, targetImgHeight, false);
        leftClick(300, true);
    }

    /**
     * 点击一个匹配点
     * @param targetImgPath 目标图路径
     * @param random 是否随机处理
     * @param isSimilar 是否近似匹配
     * @param delay 是否延时
     */
    public void clickFirst(String targetImgPath, boolean random, boolean isSimilar, boolean delay) {
        match(targetImgPath, isSimilar);
        // 点击
        if (results.isEmpty()) {
            return;
        }
        MatchResult matchResult = results.get(0);
        mouseMove(matchResult.locationX, matchResult.locationY, random, targetImgWidth, targetImgHeight, delay);
        leftClick(null, delay);
    }



    /**
     * 阻塞点击
     * @param targetImgPath 目标图路径
     * @param random 是否随机处理
     * @param num 点击数量
     * @param isSimilar 是否近似匹配
     * @return  是否成功
     */
    @SneakyThrows
    public boolean clickBlocking(String targetImgPath, boolean random, Integer num, boolean isSimilar) {
        match(targetImgPath, isSimilar);
        // 点击
        if (results.isEmpty()) {
            return false;
        }

        if (results.size() < num) {
            return false;
        }

        Thread.sleep(1000);

        IntStream.range(0, num)
                .forEach(cur-> {
                    MatchResult matchResult = results.get(cur);
                    mouseMove(matchResult.locationX, matchResult.locationY, random, targetImgWidth, targetImgHeight, false);
                    leftClick(300, true);
                });
        return true;
    }





















    /**
     * 鼠标左键单击
     * @param time 按下持续时间
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
     * @param x x坐标
     * @param y y坐标
     * @param random 是否需要进行随机处理
     * @param targetImgWidth 图片宽度
     * @param targetImgHeight 图片高度
     * @param delay 是否需要延时
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


    /**
     * 匹配
     *
     * @param targetImgPath 目标图片
     * @param isSimilar 是否近似匹配
     */
    @SneakyThrows
    private void match(String targetImgPath, boolean isSimilar) {

        reload(targetImgPath);

        Assert.isTrue(srcImgRGBData.length != 0 && targetImgRGBData.length != 0, "来源图RGB或目标图RGB数据为空");

        if (!CollectionUtils.isEmpty(results)) {
            results.clear();
        }

        if (isSimilar) {
            // 近似匹配规则：
            // 灰度匹配，相似度超过阈值即认定为匹配成功
            for (int y = 0; y < srcImgHeight - targetImgHeight; y++) {
                for (int x = 0; x < srcImgWidth - targetImgWidth; x++) {

                    // 首个位点相同时 进一步判断，节省性能，加快速度
                    if (srcImgRGBData[y][x] == targetImgRGBData[0][0]) {
                        BufferedImage image = robot.createScreenCapture(new Rectangle(x, y, targetImgWidth, targetImgHeight));
                        // 相似度
                        double similarity = ImageSimilarity.calSimilarity(image, targetImg);
                        logger.error("相似度为" + similarity);
                        if (similarity > SIMILAR_THRESHOLD) {
                            //y
                            int maxY = y + targetImgHeight;
                            int locationY = ((y + maxY) / 2);
                            //x
                            int maxX = x + targetImgWidth;
                            int locationX = ((x + maxX) / 2);
                            results.add(new Matcher.MatchResult(locationX, locationY));
                            x += targetImgWidth;
                            y += targetImgHeight;
                        }
                    }

                }
            }
        } else {

            // 精确匹配规则：
            //根据目标图的尺寸，得到目标图四个角映射到屏幕截图上的四个点，
            //判断截图上对应的四个点与图B的四个角像素点的值是否相同，
            //如果相同就将屏幕截图上映射范围内的所有的点与目标图的所有的点进行比较。 --- 暂时舍弃

            for (int y = 0; y < srcImgHeight - targetImgHeight; y++) {
                for (int x = 0; x < srcImgWidth - targetImgWidth; x++) {

                    if ((targetImgRGBData[0][0] ^ srcImgRGBData[y][x]) == 0
                            && (targetImgRGBData[0][targetImgWidth - 1] ^ srcImgRGBData[y][x + targetImgWidth - 1]) == 0
                            && (targetImgRGBData[targetImgHeight - 1][targetImgWidth - 1] ^ srcImgRGBData[y + targetImgHeight - 1][x + targetImgWidth - 1]) == 0
                            && (targetImgRGBData[targetImgHeight - 1][0] ^ srcImgRGBData[y + targetImgHeight - 1][x]) == 0) {
                        logger.error("found one!!!!");
                        //如果比较结果完全相同，则说明图片找到，填充查找到的位置坐标数据到查找结果数组。
//                        boolean found = isMatchAll(y, x);
                        boolean found = true;
                        if (found) {
                            //y
                            int maxY = y + targetImgHeight;
                            int locationY = ((y + maxY) / 2);
                            //x
                            int maxX = x + targetImgWidth;
                            int locationX = ((x + maxX) / 2);
                            results.add(new Matcher.MatchResult(locationX, locationY));

                        }
                    }
                }
            }

        }
        if (!CollectionUtils.isEmpty(results)) {
            logger.info(String.format("匹配图片：%s，结果：%s", targetImgPath, JSON.toJSONString(results)));
        }

    }


    /**
     * 初始化
     */
    private void reload(String targetImgPath) {

        if (StringUtils.hasText(targetImgPath)) {


            // 查询BufferedImage缓存
            if (imgMap.containsKey(targetImgPath)) {
                targetImg = imgMap.get(targetImgPath);
            } else {
                // 加载并刷新缓存
                this.targetImg = getBfImageFromPath(targetImgPath);
                imgMap.put(targetImgPath, targetImg);
            }
            // 查询RGB缓存
            if (RGBDataMap.containsKey(targetImgPath)) {
                targetImgRGBData = RGBDataMap.get(targetImgPath);
            } else {
                // 加载，并刷新缓存
                this.targetImgRGBData = getImageRGB(targetImg);
                RGBDataMap.put(targetImgPath, targetImgRGBData);

            }
            this.targetImgHeight = targetImg.getHeight();
            this.targetImgWidth = targetImg.getWidth();


        }

        if (StringUtils.hasText(srcImgPath)) {
            if (srcImgRGBData == null) {
                this.srcImg = getBfImageFromPath(srcImgPath);
                this.srcImgHeight = srcImg.getHeight();
                this.srcImgWidth = srcImg.getWidth();
                this.srcImgRGBData = getImageRGB(srcImg);
            }
        } else {
            reloadScreen();
        }
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

        this.srcImg = bufferedImage;
        this.srcImgWidth = bufferedImage.getWidth();
        this.srcImgHeight = bufferedImage.getHeight();
        this.srcImgRGBData = getImageRGB(bufferedImage);

    }

    /**
     * 全屏截图
     *
     * @return 返回BufferedImage
     */
    private BufferedImage getFullScreenShot() {
        BufferedImage bfImage = null;

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

    /**
     * 匹配结果静态内部类
     */
    private static class MatchResult {

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