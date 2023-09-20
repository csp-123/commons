package com.commons.onmyoji.utils;

import com.commons.onmyoji.entity.MatchResult;
import com.commons.onmyoji.entity.MatchResultItem;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import javafx.scene.layout.BackgroundImage;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @description: 测试
 * @author: cccsp
 * @date: 2023/2/23 17:08
 */
public class ForTest {


    @SneakyThrows
    public static void main(String[] args) {

//        GameWindowSnapshot instance = GameWindowSnapshot.getInstance();
//        instance.setX(1280)
//                .setY(40)
//                .setWindowWidth(1280)
//                .setWindowHeight(720);
//        String s = "D:\\learn\\IdeaProjects\\commons\\onmyoji\\src\\main\\java\\com\\commons\\onmyoji\\utils\\start_solo.png";
//
//        Matcher matcher = new Matcher(Collections.singletonList(s));
//
//        matcher.matchOne(s, true);
//
//        System.out.println(JSON.toJSONString(matcher.getResults()));
//
//        try {
//            String line;
////            Process p = Runtime.getRuntime().exec("ps -e");
//            Process p = Runtime.getRuntime().exec
//                    (System.getenv("windir") + "\\system32\\" + "tasklist.exe");
//            BufferedReader input =
//                    new BufferedReader(new InputStreamReader(p.getInputStream()));
//            while ((line = input.readLine()) != null) {
//                System.out.println(line); //<-- Parse data here.
//            }
//            input.close();
//        } catch (Exception err) {
//            err.printStackTrace();
//        }
//
//
//
        // 获取窗口句柄

//        User32 instance = User32.INSTANCE;
//        WinDef.HWND hwnd = instance.FindWindow(null, "今时月");
//        if (hwnd != null) {
//            // 获取窗口大小
//            WinDef.RECT rect = new WinDef.RECT();
//            instance.GetWindowRect(hwnd, rect);
//            int width = rect.right - rect.left;
//            int height = rect.bottom - rect.top;
//            System.out.println("窗口大小: " + width + "x" + height);
//
//            Pointer pointer = hwnd.getPointer();
//
//            // 获取鼠标坐标
//            WinDef.POINT point = new WinDef.POINT();
//            instance.GetCursorPos(point);
//
//
//            System.out.println("鼠标坐标: (" + point.x + ", " + point.y + ")");
//            System.out.println("窗口坐标: (" + rect.top + ", " + rect.bottom + ")");
//            System.out.println("窗口坐标: (" + rect.left + ", " + rect.right + ")");
//        } else {
//            System.out.println("找不到窗口");
//        }

        System.out.println(TimeUnit.DAYS.toString());

//        System.out.println(matchResultItem.getWindowName());
    }
}
