package com.commons.onmyoji.components;

import com.commons.onmyoji.config.OnmyojiConfig;
import com.commons.onmyoji.entity.GameWindowSnapshot;
import com.commons.onmyoji.entity.GameWindowSnapshotItem;
import com.commons.onmyoji.entity.MatchResult;
import com.commons.onmyoji.entity.MatchResultItem;
import com.commons.onmyoji.utils.ImageSimilarityUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.image.BufferedImage;
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
@Setter
@Component
public class Matcher {

    private static final Logger logger = LoggerFactory.getLogger(Matcher.class);

    private ExecutorService executorService = new ThreadPoolExecutor(3, 10, 2, TimeUnit.MINUTES, new ArrayBlockingQueue<>(20));

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
    private final List<String> targetImgPathList = new ArrayList<>();


    public void init(List<String> targetImgPathList) {
        List<String> imgPathList = this.getTargetImgPathList();
        imgPathList.addAll(targetImgPathList);
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
        List<GameWindowSnapshotItem> snapshotItemList = snapshot.getSnapshotItemList();
        if (snapshotItemList.isEmpty()) {
            return;
        }
        // 遍历窗口快照列表
        snapshotItemList.forEach(snapshotItem -> {
            boolean matched = match(targetImgPath, solo, snapshotItem);
            logger.info("窗口[{}]匹配结果：[{}]", snapshotItem.getWindowName(), matched);
        });
    }

    /**
     * 近似匹配规则：
     * 灰度匹配，相似度超过阈值即认定为匹配成功
     * 单刷模式下 找到一个点位即返回
     *
     * @param targetImgPath 目标图片
     * @param solo 是否单刷
     * @param snapshotItem 窗口
     * @return true or false 是否匹配到结果
     */
    @SneakyThrows
    private boolean match(String targetImgPath, boolean solo, GameWindowSnapshotItem snapshotItem) {
        int[][] RGBData = RGBDataMap.get(targetImgPath);
        BufferedImage bufferedImage = bfImageMap.get(targetImgPath);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[][] rgbData = snapshotItem.getRGBData();

        boolean found = false;

        for (int y = 0; y < rgbData.length; y++) {
            for (int x = 0; x < rgbData[0].length; x++) {
                // 与待匹配的图片的左上角像素点不匹配则跳过
                if (rgbData[y][x] != RGBData[0][0]) {
                    continue;
                }
                int realX = x + snapshotItem.getX();
                int realY = y + snapshotItem.getY();
                BufferedImage screenCaptureBfImage = robot.createScreenCapture(new Rectangle(realX, realY, width, height));
                double similarity = ImageSimilarityUtil.calSimilarity(screenCaptureBfImage, bufferedImage);
                if (similarity >= onmyojiConfig.getThreshold()) {
                    found = true;
                    MatchResult matchResult = MatchResult.getInstance();
                    Set<MatchResultItem> matchResultItems = matchResult.getResultItemMap().get(targetImgPath);
                    if (CollectionUtils.isEmpty(matchResultItems)) {
                        matchResultItems = new HashSet<>();
                    }
                    MatchResultItem resultItem = new MatchResultItem(snapshotItem.getWindowName(), realX, realY, width, height);
                    matchResultItems.add(resultItem);
                    if (solo) {
                        return found;
                    }
                }
            }
        }
        return found;
    }

    /**
     * 匹配全部图片
     *
     * @param solo
     */
    @SneakyThrows
    public Boolean matchAllImg(boolean solo) {
        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();
        for (String targetImgPath : getTargetImgPathList()) {
            CompletableFuture<Void> completableFuture =
                    CompletableFuture.runAsync(() -> matchOneImg(targetImgPath, solo), executorService);
            completableFutureList.add(completableFuture);
            completableFuture.get();
        }
        CompletableFuture<Void> all = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[getTargetImgPathList().size()]));
        // 匹配所有图片 等待时间1秒，过长匹配结果无意义
        all.get(1, TimeUnit.SECONDS);
        return all.isDone();
    }

}