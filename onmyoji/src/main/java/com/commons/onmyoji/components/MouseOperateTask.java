package com.commons.onmyoji.components;

import com.alibaba.fastjson.JSON;
import com.commons.onmyoji.entity.MatchResult;
import com.commons.onmyoji.entity.MatchResultItem;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.*;

/**
 * 鼠标操作定时器
 * @author chishupeng
 * @date 2023/8/23 3:49 PM
 */
@Component
@Slf4j
public class MouseOperateTask extends TimerTask {

    @Resource
    private Robot robot;

    @Override
    public void run() {
        MatchResult matchResult = MatchResult.getInstance();
        Map<String, Set<MatchResultItem>> resultItemMap = matchResult.getResultItemMap();
        if (resultItemMap.isEmpty()) {
            return;
        }
        Map<String, Integer> clickCountMap = matchResult.getClickCountMap();
        // 按各图片遍历匹配结果
        for (Map.Entry<String, Set<MatchResultItem>> entry : resultItemMap.entrySet()) {
            // 图片路径
            String targetImgPath = entry.getKey();
            // 所有窗口的匹配结果
            Set<MatchResultItem> resultItems = entry.getValue();
            if (CollectionUtils.isEmpty(resultItems)) {
                continue;
            }
            // 点击并移除匹配结果
            Iterator<MatchResultItem> iterator = resultItems.iterator();
            while (iterator.hasNext()) {
                clickImg(iterator.next(), true);
                iterator.remove();
            }
            resultItemMap.put(targetImgPath, resultItems);
            // 点击数++
            Integer count = clickCountMap.get(targetImgPath);
            if (count == null) {
                count = 0;
            }
            count++;
            clickCountMap.put(targetImgPath, count);
        }
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
}
