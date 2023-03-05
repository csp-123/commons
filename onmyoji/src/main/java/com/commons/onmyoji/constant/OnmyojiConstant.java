package com.commons.onmyoji.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 常量类
 * @author: cccsp
 * @date: 2023/2/23 16:31
 */
public class OnmyojiConstant {


    // 通用图片位置
    public static String COMMON_IMG_PATH = "onmyoji/src/main/resources/img/common/";

    // 御魂相关图片位置
    public static String ROYAL_SOUL_IMG_PATH = "onmyoji/src/main/resources/img/royalsoul/";


    // 庭院灯笼图片名称
    public static String YARD_LANTERN = "yardLantern.png";

    // 返回按钮
//    public static String BACK_BUTTON = "backButton.png";
//    public static String BACK_BUTTON2 = "backButton2.png";
//
//    public static String BACK_BUTTON3 = "backButton3.png";

    public static List<String> BACK_BUTTONS = new ArrayList<String>() {{
        add("backButton.png");
        add("backButton2.png");
        add("backButton3.png");
        add("backButton4.png");
        add("backButton5.png");
        add("backButton6.png");
        add("backButton7.png");
        add("backButton8.png");
        add("backButton9.png");
        add("backButton10.png");
        add("backButton11.png");
        add("backButton12.png");
        add("backButton13.png");

    }};

    public static String ROYAL_IMG_PATH = "onmyoji/src/main/resources/img/royalsoul/";


    /**
     * 单刷开始图片
     */
    public static String ROYAL_SOUL_SOLO_START_BUTTON = "solo.png";

    /**
     * 组队开始图片
     */
    public static String ROYAL_SOUL_TEAM_START_BUTTON = "team.png";

    /**
     * 结束图片
     */
    public static String ROYAL_SOUL_END_BUTTON = "end.png";

    /**
     * 组队结束图片
     */
    public static String ROYAL_SOUL_TEAM_END_BUTTON = "teamEnd.png";

    /**
     * 获取奖励图片
     */
    public static String ROYAL_SOUL_REWARD_BUTTON = "reward.png";


}
