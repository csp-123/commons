package com.commons.onmyoji.components;

import com.commons.onmyoji.constant.OnmyojiConstant;

/**
 * @description: 守护线程 - 处理异常事件
 * @author: cccsp
 * @date: 2023/3/15 13:43
 */
public class OnmyojiDeamonTask extends Thread {

    @Override
    public void run() {

        while (true) {

            String imgDirectory = System.getProperty("user.dir") + "\\" + OnmyojiConstant.COMMON_IMG_PATH + OnmyojiConstant.OFFER_REWARD;
            System.out.println(imgDirectory);
            // 好友悬赏邀请
            boolean match = ImgMatcher2.match(imgDirectory, 1);
            if(match) {
                ImgMatcher2.ClickImgRU(imgDirectory);
            }

        }

    }
}
