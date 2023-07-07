package com.commons.onmyoji.matcher;

import com.commons.onmyoji.utils.ImageSimilarity;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;

/**
 * Description:
 * 单刷图片匹配器
 * Author: chish
 * Date: 2023/3/12 1:07
 */
@Getter
public class SimilarMatcher {

    private static final Logger logger = LoggerFactory.getLogger(SimilarMatcher.class);

    private ExecutorService executorService = new ThreadPoolExecutor(3, 10, 2, TimeUnit.MINUTES, new ArrayBlockingQueue<>(20));

    private static final Robot robot = getRobot();

    // 近似匹配阈值
    private static final double SIMILAR_THRESHOLD = 0.9D;

    /**
     * BufferedImage数据缓存
     */
    private static Map<String, int[][]> RGBDataMap = new HashMap<>(16);


    /**
     * BufferedImage数据缓存
     */
    private static Map<String, BufferedImage> bfImageMap = new HashMap<>(16);

    /**
     * 全屏高
     */
    private int fullScreenHeight;

    /**
     * 全屏宽
     */
    private int fullScreenWidth;

    /**
     * 来源图片路径
     */
    private List<String> targetImgPathList;

    /**
     * 匹配结果
     */
    private Map<String, List<MatchResult>> results;

    public SimilarMatcher(List<String> targetImgPathList) {
        GraphicsDevice graphDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode disMode = graphDevice.getDisplayMode();
        int width = disMode.getWidth();
        int height = disMode.getHeight();
        this.fullScreenWidth = width;
        this.fullScreenHeight = height;
        this.load(targetImgPathList);
    }

    /**
     * 初始化加载
     *
     * @param targetImgPathList
     */
    private void load(List<String> targetImgPathList) {
        // 加载RGB缓存
        for (String targetImgPath : targetImgPathList) {
            BufferedImage bfImage = getBfImageFromPath(targetImgPath);
            int[][] imageRGB = getImageRGB(bfImage);
            bfImageMap.put(targetImgPath, bfImage);
            RGBDataMap.put(targetImgPath, imageRGB);
        }
    }

    /**
     * 匹配并点击
     */
    public void matchAndClick(boolean solo) {
        for (String targetImgPath : targetImgPathList) {
            CompletableFuture<Void> cf = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        similarMatchAndClick(targetImgPath, solo);
                    }
                }
            }, executorService);

        }

    }

    @SneakyThrows
    public void similarMatchAndClick(String targetImgPath, boolean solo) {
        int[][] srcImgRGBData = reloadScreenRGB();
        int[][] RGBData = RGBDataMap.get(targetImgPath);
        BufferedImage bufferedImage = bfImageMap.get(targetImgPath);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        // 近似匹配规则：
        // 灰度匹配，相似度超过阈值即认定为匹配成功
        // 单刷模式下 找到一个点位即返回
        outer:
        for (int y = 0; y < fullScreenHeight - height; y++) {
            boolean found = false;

            for (int x = 0; x < fullScreenWidth - width; x++) {
                // 首个位点相同时 进一步判断，节省性能，加快速度
                if (srcImgRGBData[y][x] == RGBData[0][0]) {

                    BufferedImage screenCaptureBfImage = robot.createScreenCapture(new Rectangle(x, y, width, height));
                    // 相似度
                    double similarity = ImageSimilarity.calSimilarity(screenCaptureBfImage, bufferedImage);
                    if (similarity > SIMILAR_THRESHOLD) {
                        found = true;
                        //y
                        int resultY = buildRandomLocation(y, height);
                        //x
                        int resultX = buildRandomLocation(x, width);
                        List<MatchResult> matchResults = results.get(targetImgPath);
                        if (matchResults == null) {
                            matchResults = new ArrayList<>();
                        }
                        matchResults.add(new SimilarMatcher.MatchResult(resultX, resultY));
                        // 匹配成功后横向跳过此块区域
                        x += width;
                        if (solo) {
                            break outer;
                        }
                    }
                }
            }
            if (found) {
                // 匹配成功后纵向跳过此区域
                y += height;
            }
        }
        // 点击
        results.get(targetImgPath)
                // 移动鼠标并点击
                .forEach(matchResult -> {
                    mouseMove(matchResult.locationX, matchResult.locationY, true);
                    leftClick(300, true);
                });


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
     * @param x     x坐标
     * @param y     y坐标
     * @param delay 是否需要延时
     */
    private static void mouseMove(int x, int y, boolean delay) {
        robot.mouseMove(x, y);
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
     * 获取屏幕RGB数据
     */
    private int[][] reloadScreenRGB() {
        BufferedImage bufferedImage = getFullScreenShot();
        return getImageRGB(bufferedImage);

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