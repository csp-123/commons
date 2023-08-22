package com.commons.onmyoji.matcher;

import com.alibaba.fastjson.JSON;
import com.commons.onmyoji.entity.ScreenSnapshot;
import com.commons.onmyoji.entity.ScreenSnapshotItem;
import com.commons.onmyoji.utils.ImageSimilarityUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Description:
 * 单刷图片匹配器
 * Author: chish
 * Date: 2023/3/12 1:07
 */
@Getter
public class SimilarMatcher2 {

    private static final Logger logger = LoggerFactory.getLogger(SimilarMatcher2.class);

    private final ExecutorService executorService = new ThreadPoolExecutor(1, 3, 2, TimeUnit.MINUTES, new ArrayBlockingQueue<>(20));

    private static final Robot robot = getRobot();

    // 近似匹配阈值
    private static final double SIMILAR_THRESHOLD = 0.9D;

    private static final double VERY_CLOSE_THRESHOLD = 0.7D;

    private static final double CLOSE_THRESHOLD = 0.5D;

    private static final double FARAWAY_THRESHOLD = 0.3D;

    private static final double NOWAY_THRESHOLD = 0.1D;

    /**
     * BufferedImage数据缓存
     */
    private static final Map<String, int[][]> RGBDataMap = new HashMap<>(16);


    /**
     * BufferedImage数据缓存
     */
    private static final Map<String, BufferedImage> bfImageMap = new HashMap<>(16);

    /**
     * 来源图片路径
     */
    private final List<String> targetImgPathList;

    /**
     * 匹配结果 keu-imgPath, value-Map:key-windowName,value-result
     */
    private Map<String, Map<String, MatchResult>> results = new HashMap<>();

    /**
     * 匹配结果
     */
    private Map<String, Integer> count = new HashMap<>();

    private List<ScreenSnapshot> snapshots = new ArrayList<>();

    public SimilarMatcher2(List<String> targetImgPathList) {
        this.targetImgPathList = targetImgPathList;
        this.load(targetImgPathList);
        this.count = targetImgPathList.stream()
                .collect(Collectors.toMap(o -> o, v -> 0));
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

    @SneakyThrows
    public void matchOne(String targetImgPath, boolean solo) {

        int[][] RGBData = RGBDataMap.get(targetImgPath);
        BufferedImage bufferedImage = bfImageMap.get(targetImgPath);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        // 近似匹配规则：
        // 灰度匹配，相似度超过阈值即认定为匹配成功
        // 单刷模式下 找到一个点位即返回
        ScreenSnapshot snapshot = ScreenSnapshot.getInstance();
        if (snapshot.getSnapshotItemMap().isEmpty()) {
            return;
        }
        for (Map.Entry<String, ScreenSnapshotItem> snapshotItemEntry : snapshot.getSnapshotItemMap().entrySet()) {
            ScreenSnapshotItem snapshotItem = snapshotItemEntry.getValue();

            int[][] rgbData = snapshotItem.getRGBData();
            for (int y = 0; y < rgbData.length; y++) {
                for (int x = 0; x < rgbData[0].length; x++) {
                    if (rgbData[y][x] != RGBData[0][0]) {
                        continue;
                    }
                    int realX = x + snapshotItem.getX();
                    int realY = y + snapshotItem.getY();
                    BufferedImage screenCaptureBfImage = robot.createScreenCapture(new Rectangle(realX, realY, width, height));
                    double similarity = ImageSimilarityUtil.calSimilarity(screenCaptureBfImage, bufferedImage);
                    if (similarity >= SIMILAR_THRESHOLD) {
                        Map<String, MatchResult> matchResultMap = results.get(targetImgPath);
                        if (matchResultMap == null) {
                            matchResultMap = new HashMap<>();
                        }
                        logger.info("result:{}", JSON.toJSONString(matchResultMap));
                        MatchResult result = new MatchResult(realX, realY, width, height);
                        matchResultMap.put(snapshotItemEntry.getKey(), result);
                        results.put(targetImgPath, matchResultMap);
                        if (solo) {
                            return;
                        }
                    }
                }
            }
        }

    }

    public void matchAll(boolean solo, Long endTime) {
        while (System.currentTimeMillis() <= endTime) {
            for (String targetImgPath : getTargetImgPathList()) {
                matchOne(targetImgPath, solo);
            }
        }
    }

    public void clickAll(boolean solo, Long endTime) {
        Map<String, Map<String, MatchResult>> map = getResults();
        while (System.currentTimeMillis() <= endTime) {
            if (map.isEmpty()) {
                continue;
            }
            map.keySet()
                    .forEach(imgPath -> clickOne(imgPath, solo));
        }
    }

    @SneakyThrows
    public boolean clickOne(String targetImgPath, boolean solo) {
        logger.info("执行一次点击");
        Map<String, MatchResult> matchResultMap = results.get(targetImgPath);
        if (CollectionUtils.isEmpty(matchResultMap)) {
            return false;
        }
        Integer imgClickCount = count.get(targetImgPath);
        count.put(targetImgPath, imgClickCount == null ? 0 : ++imgClickCount);

        matchResultMap.values().forEach(matchResult -> {
            int randomX = buildRandomLocation2(matchResult.getLocationX(), matchResult.getImgWidth());
            int randomY = buildRandomLocation2(matchResult.getLocationY(), matchResult.getImgHeight());
            click(randomX, randomY);
            matchResultMap.clear();
        });
        return true;
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
     * 以中心点为构建随机位置
     * 例：（125，226） 64*58 横坐标：position = 125，横向长度： size = 64
     * 得出最终横坐标 125-(64/2)<result<125+(64/2) 即   93<result<157
     *
     * @param position 像素单维度坐标
     * @param length   图像单维度长度
     * @return 最终坐标
     */
    private static int buildRandomLocation2(int position, int length) {
        Random random = new Random();
        int max = position + length;
        return random.nextInt(max - position + 1) + position;
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

    public void click(int x, int y) {
        mouseMove(x, y, true);
        leftClick(300, true);
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

    @Getter
    @Setter
    private static class MatchResult {

        /**
         * x坐标
         */
        private Integer locationX;

        /**
         * Y坐标
         */
        private Integer locationY;

        /**
         * 宽
         */
        private Integer imgWidth;

        /**
         * 高
         */
        private Integer imgHeight;

        MatchResult(Integer x, Integer y, Integer width, Integer height) {
            locationX = x;
            locationY = y;
            imgWidth = width;
            imgHeight = height;

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