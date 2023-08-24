package com.commons.onmyoji.components;

import com.commons.onmyoji.entity.GameWindowSnapshot;
import com.commons.onmyoji.entity.GameWindowSnapshotItem;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * 游戏窗口刷新：实时监测游戏窗口位置、窗口大小
 *
 * @author chishupeng
 * @date 2023/8/18 2:45 PM
 */
@Data
@Component
@Slf4j
public class GameWindowFreshTask extends TimerTask {

    /**
     * 窗口名列表
     */
    private List<String> windowsNameList;

    @Resource
    private Robot robot;


    @Override
    public void run() {

        Assert.notEmpty(windowsNameList, "未指定窗口名称");

        GameWindowSnapshot instance = GameWindowSnapshot.getInstance();

        for (String windowName : windowsNameList) {
            reloadScreenSnapShot(robot, windowName, instance);
        }
        log.info("游戏窗口刷新完成，监测到当前游戏窗口数：{}，窗口信息：{}", windowsNameList.size(), instance.getSnapshotItemList().stream().map(GameWindowSnapshotItem::toString).reduce((item1,item2) -> item1 + "|" + item2).orElse(""));

    }

    private void reloadScreenSnapShot(Robot robot, String windowName, GameWindowSnapshot snapshot) {
        WinDef.RECT rect = getRect(windowName);
        GameWindowSnapshotItem snapshotItem = new GameWindowSnapshotItem();
        snapshotItem.setX(rect.left);
        snapshotItem.setY(rect.top);
        snapshotItem.setWindowName(windowName);
        snapshotItem.setWindowWidth(rect.right - rect.left);
        snapshotItem.setWindowHeight(rect.bottom - rect.top);
        snapshotItem.setX(rect.left);
        BufferedImage screenCapture = robot.createScreenCapture(new Rectangle(snapshotItem.getX(), snapshotItem.getY(), snapshotItem.getWindowWidth(), snapshotItem.getWindowHeight()));
        snapshotItem.setBufferedImage(screenCapture);
        snapshotItem.setRGBData(getImageRGB(screenCapture));
        snapshot.getSnapshotItemList().add(snapshotItem);
    }

    private WinDef.RECT getRect(String windowName) {
        // 记录游戏窗口位置、大小
        User32 user32 = User32.INSTANCE;
        WinDef.HWND hwnd = user32.FindWindow(null, windowName);
        Assert.notNull(hwnd, "找不到窗口");
        // 获取窗口大小
        WinDef.RECT rect = new WinDef.RECT();
        user32.GetWindowRect(hwnd, rect);
        return rect;
    }

    /**
     * 根据BufferedImage获取图片RGB数组
     *
     * @param bfImage
     * @return
     */
    private int[][] getImageRGB(BufferedImage bfImage) {
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
}
