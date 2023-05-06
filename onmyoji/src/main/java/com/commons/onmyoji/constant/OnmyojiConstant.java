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
    public static final String COMMON_IMG_PATH = "onmyoji/src/main/resources/img/common/";


    // 庭院加成灯笼图片名称
    public static final String YARD_LANTERN = "yardLantern.png";

    // 庭院探索灯笼图片名称
//    public static final String TAN_SUO_YARD_LANTERN = "tanSuoLantern.png";



    // 被好友邀请悬赏图片
    public static final String OFFER_REWARD = "offerReward.png";

    // 返回按钮
    public static List<String> BACK_BUTTONS = new ArrayList<String>() {{
        add("backButton.png");
        add("backButton1.png");
        add("backButton2.png");
        add("backButton3.png");
        add("backButton4.png");
        add("backButton5.png");
        add("backButton6.png");
//        add("backButton7.png");
//        add("backButton8.png");
//        add("backButton9.png");
//        add("backButton10.png");
//        add("backButton11.png");
//        add("backButton12.png");
//        add("backButton13.png");

    }};

    // 御魂 =================================================================================================================

    /**
     * 开始图片
     */
    public static String ROYAL_SOUL_START_BUTTON = "start.png";


    /**
     * 获取奖励图片
     */
    public static String ROYAL_SOUL_REWARD_BUTTON = "reward.png";

    /**
     * 结束图片
     */
    public static String ROYAL_SOUL_END_BUTTON = "end.png";





    // 困28 =================================================================================================================

    /**
     * 开始探索按钮
     */
    public static String TAN_SUO_START_BUTTON = "startTanSuo_solo.png";


    /**
     * 挑战怪物按钮
     */
    public static String TAN_SUO_CHALLENGE_BUTTON = "challenge.png";

    /**
     * 挑战boss按钮
     */
    public static String TAN_SUO_CHALLENGE_BOSS_BUTTON = "challengeBoss.png";

    /**
     * 奖励按钮
     */
    public static String TAN_SUO_REWARD_BUTTON = "reward.png";

    /**
     * 结束
     */
    public static String TAN_SUO_END_BUTTON = "end.png";

    /**
     * 结界突破界面
     */
    public static String TAN_SUO_DEMARCATION_BUTTON = "demarcation.png";

    /**
     * 结界票满
     */
    public static String TAN_SUO_FULL_DEMARCATION_BUTTON = "fullDemarcation.png";


    public static String TAN_SUO_MOVE_POINT_BUTTON = "movePoint.png";


    /**
     * 关闭探索准备界面
     */
    public static String TAN_SUO_CLOSE_BUTTON = "closeTanSuo.png";

    /**
     * 28章
     */
    public static String TAN_SUO_CHAPTER_28_BUTTON = "chapter28.png";


    /**
     * 结束探索
     */
    public static String TAN_SUO_END_TANSUO_BUTTON = "endTanSuo.png";

    /**
     * 确认结束探索
     */
    public static String TAN_SUO_CONFIRM_END_TANSUO_BUTTON = "confirmEndTanSuo.png";
}
