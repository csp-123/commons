package com.commons.onmyoji.components;


import com.commons.onmyoji.entity.*;
import com.commons.onmyoji.job.RunningJobMatchResultPool;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.CompletableFuture;

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
public class MatcherV2 {

    @Resource
    private Robot robot;

    @Resource
    private RunningJobMatchResultPool runningJobMatchResultPool;

    /**
     * 匹配成功的map
     * Map<imgName, Map<windowName, PicLocation>>
     */
    private Map<String, Map<String, PicLocation>> matchedResultRecord = new HashMap<>();

    /**
     * 近似阈值
     */
    @Value("${onmyoji.threshold}")
    private Double threshold;

    /**
     * 缩放比例
     */
    @Value("${onmyoji.scale}")
    private Double scale;

    /**
     * 匹配单张图片
     *
     * @param picture
     */
    public void matchOneImg(Picture picture, OnmyojiJob onmyojiJob) {
        // 获取快照信息
        GameWindowSnapshot snapshot = GameWindowSnapshot.getInstance();
        Set<GameWindowSnapshotItem> snapshotItemList = snapshot.getSnapshotItemList();
        if (snapshotItemList.isEmpty()) {
            log.info("窗口信息为空");
            return;
        }
        // 遍历窗口
        snapshotItemList.forEach(snapshotItem -> doMatch(snapshotItem, picture, onmyojiJob));
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
     * 单刷模式下 找到一个匹配结果即返回
     *
     * @param pic          目标图片
     * @param snapshotItem 窗口
     */
    private void doMatch(GameWindowSnapshotItem snapshotItem, Picture pic, OnmyojiJob job) {
        // 匹配屏幕区域RGB数据
        int[][] rgbData = snapshotItem.getRGBData();
        // 待匹配图片RGB数据
        int[][] RGBData = pic.getRgb();
        BufferedImage bufferedImage = pic.getBf();
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        boolean found = false;

        // 该图片已有匹配成功的记录
        if (matchedResultRecord.containsKey(pic.getImgName())) {
            Map<String, PicLocation> picLocationMap = matchedResultRecord.get(pic.getImgName());
            // 该图片已有当前窗口匹配成功记录
            if (picLocationMap.containsKey(snapshotItem.getWindowName())) {
                PicLocation picLocation = picLocationMap.get(snapshotItem.getWindowName());
                int top = picLocation.getTop();
                int left = picLocation.getLeft();
                int right = picLocation.getRight();
                int bottom = picLocation.getBottom();
                // 矩形四个顶点匹配成功
                if ((RGBData[0][0] ^ rgbData[top][left]) == 0
                        && (RGBData[0][width - 1] ^ rgbData[top][right]) == 0
                        && (RGBData[height - 1][width - 1] ^ rgbData[bottom][right]) == 0
                        && (RGBData[height - 1][0] ^ rgbData[bottom][left]) == 0) {

                    //如果比较结果大于阈值，则说明图片找到，填充查找到的位置坐标数据到查找结果数组。
                    found = halfSimilarity(top, left, rgbData, RGBData) >= threshold;
                    if (found) {
                        // 匹配成功后的操作：
                        matchSuccess(snapshotItem, pic, job, left, top, width, height);
                    }
                }
                return;
            }
        }

        // 该图片不存在匹配成功的记录 或该图片在当前窗口内不存在匹配成功的记录
        for (int y = 0; y < rgbData.length - height; y++) {
            for (int x = 0; x < rgbData[0].length - width; x++) {
                int realX = x + snapshotItem.getX();
                int realY = y + snapshotItem.getY();
                // 矩形四个顶点匹配成功
                if ((RGBData[0][0] ^ rgbData[y][x]) == 0
                        && (RGBData[0][width - 1] ^ rgbData[y][x + width - 1]) == 0
                        && (RGBData[height - 1][width - 1] ^ rgbData[y + height - 1][x + width - 1]) == 0
                        && (RGBData[height - 1][0] ^ rgbData[y + height - 1][x]) == 0) {

                    //如果比较结果大于阈值，则说明图片找到，填充查找到的位置坐标数据到查找结果数组。
                    found = halfSimilarity(y, x, rgbData, RGBData) >= threshold;
                    if (found) {
                        // 先记录成功结果，后续直接使用该位置坐标
                        recordSuccessResult(snapshotItem, pic, realX, width, realY, height);
                        // 匹配成功后的操作
                        matchSuccess(snapshotItem, pic, job, realX, realY, width, height);
                    }
                }
            }
        }
    }

    private void matchSuccess(GameWindowSnapshotItem snapshotItem, Picture pic, OnmyojiJob job, int left, int top, int width, int height) {
        TargetMatchingResult resultItem = new TargetMatchingResult(snapshotItem.getWindowName(), pic.getImgPath(), left, top, width, height);
        if (pic.getLocate()) {
            // 鼠标点击
            clickImg(resultItem, true);
        } else {
            leftClick(300, true);
        }
        MatchResult matchResult = runningJobMatchResultPool.getMatchResultMap().get(job.getJobId());
        if (matchResult == null) {
            matchResult = new MatchResult();
            runningJobMatchResultPool.getMatchResultMap().put(job.getJobId(), matchResult);
        }
        Set<TargetMatchingResult> targetMatchingResults = matchResult.getResultItemMap().computeIfAbsent(snapshotItem.getWindowName(), k -> new HashSet<>());
        if (CollectionUtils.isEmpty(targetMatchingResults)) {
            TargetMatchingResult result = getMatchingResult(pic.getImgPath(), snapshotItem, resultItem);
            targetMatchingResults.add(result);
        } else {
            reloadMatchingResults(pic.getImgPath(), resultItem, targetMatchingResults);
        }
        // 点击后等待，防止重复匹配，导致点击计数错误
        waitSomeTime(1000, 1000);
        log.info("窗口【{}】匹配成功，匹配点位：【{},{}】", resultItem.getWindowName(), left, top);
    }

    /**
     * 记录匹配成功的结果
     * @param snapshotItem
     * @param pic
     * @param realX
     * @param width
     * @param realY
     * @param height
     */
    private void recordSuccessResult(GameWindowSnapshotItem snapshotItem, Picture pic, int realX, int width, int realY, int height) {
        PicLocation picLocation = new PicLocation(realX, realX + width, realY, realY + height);
        // 存在该图片的记录，但无该窗口下的记录
        if (matchedResultRecord.containsKey(pic.getImgName())) {
            matchedResultRecord.get(pic.getImgName()).put(snapshotItem.getWindowName(), picLocation);
        } else {
            Map<String, PicLocation> windowPicLocationMap = Maps.newHashMap();
            windowPicLocationMap.put(snapshotItem.getWindowName(), picLocation);
            matchedResultRecord.put(pic.getImgName(), windowPicLocationMap);
        }

    }

    private Double halfSimilarity(int y, int x, int[][] srcRgbData, int[][] targetRgbData) {
        double count = 0;

        int halfHeight = targetRgbData.length/2;
        int halfWidth = targetRgbData[0].length/2;
        for (int i = 0; i < halfHeight; i++) {
            for (int j = 0; j < halfWidth; j++) {
                if ((targetRgbData[i][j] ^ srcRgbData[y + i][x + j]) == 0) {
                    count++;
                }
            }
        }

        return count / (halfHeight * halfWidth);
    }

    private void reloadMatchingResults(String targetImgPath, TargetMatchingResult resultItem, Set<TargetMatchingResult> targetMatchingResults) {
        for (TargetMatchingResult targetMatchingResult : targetMatchingResults) {
            if (targetMatchingResult.getTargetImgName().equals(targetImgPath)) {
                targetMatchingResult.setCount(targetMatchingResult.getCount() + 1);
                targetMatchingResult.setLocationX(resultItem.getLocationX());
                targetMatchingResult.setLocationY(resultItem.getLocationY());
                targetMatchingResult.setImgHeight(resultItem.getImgHeight());
                targetMatchingResult.setImgWidth(resultItem.getImgWidth());
            }
        }
    }

    @NotNull
    private static TargetMatchingResult getMatchingResult(String targetImgPath, GameWindowSnapshotItem snapshotItem, TargetMatchingResult resultItem) {
        TargetMatchingResult result = new TargetMatchingResult();
        result.setWindowName(snapshotItem.getWindowName());
        result.setTargetImgName(targetImgPath);
        result.setCount(1);
        result.setLocationX(resultItem.getLocationX());
        result.setLocationY(resultItem.getLocationY());
        result.setImgHeight(resultItem.getImgHeight());
        result.setImgWidth(resultItem.getImgWidth());
        return result;
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


    public void clickImg(TargetMatchingResult targetMatchingResult, boolean random) {
        int x = targetMatchingResult.getLocationX();
        int y = targetMatchingResult.getLocationY();
        if (random) {
            x = buildRandomLocation(x, targetMatchingResult.getImgWidth());
            y = buildRandomLocation(y, targetMatchingResult.getImgHeight());
        }
        mouseMove(x, y, true);
        leftClick(300, true);
        // 点击完要不要将光标移走呢 随机移动一个位置，
        waitSomeTime(200, 400);
        int randomDistance = buildRandomLocation(150, 200) * (new Random().nextBoolean() ? 1 : -1);
        mouseMove(x + randomDistance, y + randomDistance, true);
        waitSomeTime(2000, 3000);
        mouseMove(x, y, true);
    }


    /**
     * 构建随机位置
     * 例：（125，226） 64*58 横坐标：position = 125，横向长度： size = 64
     * 得出最终横坐标 125<result<125+64 即   125<result<189
     *
     * @param position 像素单维度坐标
     * @param length   图像单维度长度
     * @return 最终坐标
     */
    private int buildRandomLocation(int position, int length) {
        Random random = new Random();
        return random.nextInt(length) + position;
    }


    public void click(int x, int y) {

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
        waitSomeTime(time/2, time);
        leftUp();
//        log.info("完成一次鼠标点击");
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
        // 处理系统缩放问题
//        int realX = (int) (x / scale);
//        int realY = (int) (y / scale);

        // 平滑移动
//        //切记！！！！！！！！！！    这里须先将坐标设置为-1
//        robot.mouseMove(-1,-1);
        int step = 1;
        while (MouseInfo.getPointerInfo().getLocation().x != x || MouseInfo.getPointerInfo().getLocation().y != y) {
            Point currentLocation = MouseInfo.getPointerInfo().getLocation();
            int moveX = 0;
            int moveY = 0;
            if (Math.abs(x - currentLocation.x) > 0) {
                moveX = step * (x - currentLocation.x > 0 ? 1 : -1);
            }
            if (Math.abs(y - currentLocation.y) > 0) {
                moveY = step * (y - currentLocation.y > 0 ? 1 : -1);
            }
            robot.mouseMove(currentLocation.x + moveX, currentLocation.y + moveY);

        }
        // 鼠标移动结束后停顿一段时间，防止被检测
        if (delay) {
            waitSomeTime(200, 400);
        }
    }

    /**
     * 等待一段时间
     *
     * @param time1
     * @param time2
     */
    private void waitSomeTime(int time1, int time2) {
        int randomTime;
        if (time1 == time2) {
            randomTime = time1;
        } else {
            Random random = new Random();
            int max = Math.max(time1, time2);
            int min = Math.max(time1, time2);
            randomTime = random.nextInt(max - min + 1) + min;
        }

        try {
            Thread.sleep(randomTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public void matchAll(OnmyojiJob job) {
        try {
            for (Picture pic : job.getPictureList()) {
                // 不能传线程池，否则后来的任务会把前面的覆盖，解决方案是每个任务新建一个线程池
                CompletableFuture<Void> completableFuture =
                        CompletableFuture.runAsync(() -> matchOneImg(pic, job));
                completableFuture.get();
            }
        } catch (Exception e) {
            log.error("匹配异常：{}", Throwables.getStackTraceAsString(e));
        }
    }


    //    缩放比例
    public int getScreenScalingFactor() {
        // 获取图形环境
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // 获取当前的图形设备，通常是主显示器
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        // 获取屏幕分辨率
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();
        // 获取默认工具包
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        // 获取屏幕尺寸（虚拟尺寸，非物理尺寸）
        Dimension screenSize = toolkit.getScreenSize();
        // 输出屏幕宽度和高度
        int Width = screenSize.width;
        int Height = screenSize.height;
        final int TARGET_WIDTH = Width;
        final int TARGET_HEIGHT = Height;
        // 计算宽度和高度的缩放比
        double widthScale = (double)screenWidth / TARGET_WIDTH;
        double heightScale = (double)screenHeight / TARGET_HEIGHT;
        // 选择更大的缩放比作为最终的缩放比
        double scale = Math.max(widthScale, heightScale);
        return (int) scale;
    }

    public void recordMouseClick(int x, int y, int scale) {
        int javaX = x / scale;
        int javaY = y / scale;
        //切记！！！！！！！！！！    这里须先将坐标设置为-1
        robot.mouseMove(-1,-1);
        robot.mouseMove(javaX,javaY);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void run(OnmyojiJob job) {
        //清空历史匹配记录
        matchedResultRecord.clear();
        //设置robot行为延时
        robot.setAutoDelay(0);
        try {
            for (Picture pic : job.getPictureList()) {
                // 不能传线程池，否则后来的任务会把前面的覆盖，解决方案是每个任务新建一个线程池
                CompletableFuture<Void> completableFuture =
                        CompletableFuture.runAsync(() -> matchOneImg(pic, job));
                completableFuture.get();
            }
        } catch (Exception e) {
            log.error("匹配异常：{}", Throwables.getStackTraceAsString(e));
        }
    }
}