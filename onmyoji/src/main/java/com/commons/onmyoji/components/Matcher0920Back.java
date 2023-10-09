package com.commons.onmyoji.components;

import com.commons.onmyoji.config.OnmyojiConfig;
import com.commons.onmyoji.entity.GameWindowSnapshot;
import com.commons.onmyoji.entity.GameWindowSnapshotItem;
import com.commons.onmyoji.entity.MatchResult;
import com.commons.onmyoji.entity.MatchResultItem;
import com.commons.onmyoji.utils.ImageSimilarityUtil;
import com.google.common.base.Throwables;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * 图片匹配器
 * Author: chish
 * Date: 2023/3/12 1:07
 */
@Getter
@Slf4j
@Setter
@Component
public class Matcher0920Back {

    @Resource
    private Robot robot;

    @Resource
    private OnmyojiConfig onmyojiConfig;


    /**
     * rgb数据缓存
     */
    private static final Map<String, int[][]> RGBDataMap = new HashMap<>(16);

    /**
     * BufferedImage数据缓存
     */
    private static final Map<String, BufferedImage> bfImageMap = new HashMap<>(16);

    /**
     * 来源图片路径
     */
    private final Set<String> targetImgPathList = new HashSet<>();

    private MatchResult matchResult = new MatchResult();


    public void init(Set<String> targetImgPathList) {
        this.targetImgPathList.addAll(targetImgPathList);
        this.load();
        log.info("匹配器初始化完成");
    }

    /**
     * 初始化加载
     */
    private void load() {
        // 加载RGB缓存
        for (String targetImgPath : targetImgPathList) {
            BufferedImage bfImage = ImageSimilarityUtil.getBfImageFromPath(targetImgPath);
            int[][] imageRGB = ImageSimilarityUtil.getImageRGB(bfImage);
            bfImageMap.put(targetImgPath, bfImage);
            RGBDataMap.put(targetImgPath, imageRGB);
        }
    }

    /**
     * 匹配单张图片
     *
     * @param targetImgPath
     * @param solo
     */
    public void matchOneImg(String targetImgPath, boolean solo) {
        // 获取快照信息
        GameWindowSnapshot snapshot = GameWindowSnapshot.getInstance();
        Set<GameWindowSnapshotItem> snapshotItemList = snapshot.getSnapshotItemList();
        if (snapshotItemList.isEmpty()) {
            return;
        }
        // 遍历窗口快照列表
        snapshotItemList.forEach(snapshotItem -> {
            boolean matched = match(targetImgPath, solo, snapshotItem);
            log.info("窗口[{}]匹配图片[{}]结果：[{}]", snapshotItem.getWindowName(), getNameFromPath(targetImgPath), matched);
        });
    }

    /**
     * 从图片路径中提取图片名称
     *
     * @param path
     * @return
     */
    private String getNameFromPath(String path) {
        Assert.hasText(path, "图片路径为空");
        try {
            String[] dotSplits = path.split("\\.");
            String pre = dotSplits[dotSplits.length - 2];
            String[] split = pre.split("\\\\");
            return split[split.length - 1];
        } catch (Exception e) {
            log.error("图片名称提取失败");
            throw new UnknownFormatConversionException(e.getMessage());
        }
    }

    /**
     * 近似匹配规则：
     * 灰度匹配，相似度超过阈值即认定为匹配成功
     * 单刷模式下 找到一个点位即返回
     *
     * @param targetImgPath 目标图片
     * @param solo          是否单刷
     * @param snapshotItem  窗口
     * @return true or false 是否匹配到结果
     */
    private boolean match(String targetImgPath, boolean solo, GameWindowSnapshotItem snapshotItem) {
        int[][] RGBData = RGBDataMap.get(targetImgPath);
        BufferedImage bufferedImage = bfImageMap.get(targetImgPath);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[][] rgbData = snapshotItem.getRGBData();

        boolean found = false;
//
//        for (int y = 0; y < rgbData.length; y++) {
//            for (int x = 0; x < rgbData[0].length; x++) {
//                // 与待匹配的图片的左上角像素点不匹配则跳过
//                if (rgbData[y][x] != RGBData[0][0]) {
//                    continue;
//                }
//                int realX = x + snapshotItem.getX();
//                int realY = y + snapshotItem.getY();
//                BufferedImage screenCaptureBfImage = robot.createScreenCapture(new Rectangle(realX, realY, width, height));
//                double similarity = ImageSimilarityUtil.calSimilarity(screenCaptureBfImage, bufferedImage);
//                log.info("img:{},x:{},y:{},similarity:{}", getNameFromPath(targetImgPath), x, y, similarity);
//                if (similarity >= onmyojiConfig.getThreshold()) {
//                    found = true;
//                    MatchResultItem resultItem = new MatchResultItem(snapshotItem.getWindowName(), realX, realY, width, height);
//                    clickImg(resultItem, true);
//                    Map<String, Integer> clickCountMap = matchResult.getClickCountMap();
//                    // 点击数++
//                    Integer count = clickCountMap.get(targetImgPath);
//                    if (count == null) {
//                        count = 0;
//                    }
//                    count++;
//                    clickCountMap.put(targetImgPath, count);
//                    matchResult.setClickCountMap(clickCountMap);
//                    if (solo) {
//                        return found;
//                    }
//                }
//            }
//        }
//
//

        for (int y = 0; y < rgbData.length - height; y++) {
            for (int x = 0; x < rgbData[0].length - width; x++) {
                int realX = x + snapshotItem.getX();
                int realY = y + snapshotItem.getY();
                if ((RGBData[0][0] ^ rgbData[y][x]) == 0
                        && (RGBData[0][width - 1] ^ rgbData[y][x + width - 1]) == 0
                        && (RGBData[height - 1][width - 1] ^ rgbData[y + height - 1][x + width - 1]) == 0
                        && (RGBData[height - 1][0] ^ rgbData[y + height - 1][x]) == 0) {

                    //如果比较结果大于阈值，则说明图片找到，填充查找到的位置坐标数据到查找结果数组。
                    found = calSimilarity(y, x, rgbData, RGBData) >= onmyojiConfig.getThreshold();

                    if (found) {
                        MatchResultItem resultItem = new MatchResultItem(snapshotItem.getWindowName(), realX, realY, width, height);
                        clickImg(resultItem, true);
                        Map<String, Integer> clickCountMap = matchResult.getClickCountMap();
                        // 点击数++
                        Integer count = clickCountMap.get(targetImgPath);
                        if (count == null) {
                            count = 0;
                        }
                        count++;
                        clickCountMap.put(targetImgPath, count);
                        matchResult.setClickCountMap(clickCountMap);
                        if (solo) {
                            return found;
                        }
                    }
                }
            }
        }
        return found;
    }

    /**
     * 近似匹配规则：
     * 灰度匹配，相似度超过阈值即认定为匹配成功
     * 单刷模式下 找到一个点位即返回
     *
     * @param targetImgPath 目标图片
     * @param solo          是否单刷
     * @param snapshotItem  窗口
     * @return true or false 是否匹配到结果
     */
    private boolean matchRU(String targetImgPath, boolean solo, GameWindowSnapshotItem snapshotItem) {
        int[][] RGBData = RGBDataMap.get(targetImgPath);
        BufferedImage bufferedImage = bfImageMap.get(targetImgPath);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[][] rgbData = snapshotItem.getRGBData();

        boolean found = false;

        for (int y = 0; y < rgbData.length - height; y++) {
            for (int x = 0; x < rgbData[0].length - width; x++) {
                int realX = x + snapshotItem.getX();
                int realY = y + snapshotItem.getY();
                if ((RGBData[0][0] ^ rgbData[y][x]) == 0
                        && (RGBData[0][width - 1] ^ rgbData[y][x + width - 1]) == 0
                        && (RGBData[height - 1][width - 1] ^ rgbData[y + height - 1][x + width - 1]) == 0
                        && (RGBData[height - 1][0] ^ rgbData[y + height - 1][x]) == 0) {

                    //如果比较结果大于阈值，则说明图片找到，填充查找到的位置坐标数据到查找结果数组。
                    found = calSimilarity(y, x, rgbData, RGBData) >= onmyojiConfig.getThreshold();

                    if (found) {
                        MatchResultItem resultItem = new MatchResultItem(snapshotItem.getWindowName(), realX, realY, width, height);
                        clickImgRU(resultItem);
                        if (solo) {
                            return found;
                        }
                    }
                }
            }
        }
        return found;
    }



    /**
     * @param y
     * @param x
     * @param srcRgbData
     * @param targetRgbData
     * @return
     */
    private Double calSimilarity(int y, int x, int[][] srcRgbData, int[][] targetRgbData) {
        double count = 0;

        int height = targetRgbData.length;
        int width = targetRgbData[0].length;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < targetRgbData[i].length; j++) {
                if ((targetRgbData[i][j] ^ srcRgbData[y + i][x + j]) == 0) {
                    count++;
                }
            }
        }
        double similarity = count / (height * width);
        log.info("similarity:{}", similarity);
        return similarity;
    }


    public void matchAll(boolean solo) {
        for (String targetImgPath : targetImgPathList) {
            matchOneImg(targetImgPath, solo);
        }
    }

    /**
     * 匹配全部图片 todo
     * 待改造，目前这种异步有问题
     *
     * @param solo
     */
    public Boolean matchAllImg(boolean solo) {
        try {
            List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();
            for (String targetImgPath : getTargetImgPathList()) {
                // 不能传线程池，否则后来的任务会把前面的覆盖，解决方案是每个任务新建一个线程池
                CompletableFuture<Void> completableFuture =
                        CompletableFuture.runAsync(() -> matchOneImg(targetImgPath, solo));
                completableFutureList.add(completableFuture);
                completableFuture.get();
            }
            CompletableFuture<Void> all = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[getTargetImgPathList().size()]));
            // 匹配所有图片 等待时间1秒，过长匹配结果无意义
            all.get(1, TimeUnit.SECONDS);
            return all.isDone();
        } catch (Exception e) {
            log.error("匹配异常：{}", Throwables.getStackTraceAsString(e));
        }
        return false;
    }


    public void clickImg(MatchResultItem matchResultItem, boolean random) {
        int x = matchResultItem.getLocationX();
        int y = matchResultItem.getLocationY();
        if (random) {
            x = buildRandomLocation(x, matchResultItem.getImgWidth());
            y = buildRandomLocation(y, matchResultItem.getImgHeight());
        }
        click(x, y);
    }

    public void clickImgRU(MatchResultItem matchResultItem) {
        int x = matchResultItem.getLocationX() + matchResultItem.getImgWidth();
        int y = matchResultItem.getLocationY();
        click(x, y);
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
    private int buildRandomLocation(int position, int length) {
        Random random = new Random();
        int min = position - length / 2;
        int max = position + length / 2;
        return random.nextInt(max - min + 1) + min;
    }


    public void click(int x, int y) {
        mouseMove(x, y, true);
        leftClick(300, true);
    }


    /**
     * 鼠标左键单击
     *
     * @param time   按下持续时间
     * @param random 按下持续时间是否进行随机处理
     */
    private void leftClick(Integer time, boolean random) {
        // 默认300
        if (time == null)
            time = 300;
        leftDown();
        int delayTime = setAutoDelay(time == 0 ? 100 : time, random);
        leftUp();
        log.info(String.format("单机鼠标左键成功！是否随机延时：[%s]", random) + (random ? String.format("，随机延时：[%s]", delayTime) : ""));
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
     * 鼠标移动
     *
     * @param x     x坐标
     * @param y     y坐标
     * @param delay 是否需要延时
     */
    private void mouseMove(int x, int y, boolean delay) {
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

    public void matchOneImgRU(String imgDirectory, boolean solo) {
        // 获取快照信息
        GameWindowSnapshot snapshot = GameWindowSnapshot.getInstance();
        Set<GameWindowSnapshotItem> snapshotItemList = snapshot.getSnapshotItemList();
        if (snapshotItemList.isEmpty()) {
            return;
        }
        // 遍历窗口快照列表
        snapshotItemList.forEach(snapshotItem -> {
            boolean matched = matchRU(imgDirectory, solo, snapshotItem);
            log.info("窗口[{}]匹配图片[{}]结果：[{}]", snapshotItem.getWindowName(), getNameFromPath(imgDirectory), matched);
        });
    }
}