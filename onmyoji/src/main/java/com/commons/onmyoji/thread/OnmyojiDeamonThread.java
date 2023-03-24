package com.commons.onmyoji.thread;

import com.commons.onmyoji.config.RoyalSoulConfig;
import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.matcher.ImgMatcher;
import com.commons.onmyoji.matcher.ImgMatcher2;

/**
 * @description: 守护线程 - 处理异常事件
 * @author: cccsp
 * @date: 2023/3/15 13:43
 */
public class OnmyojiDeamonThread extends Thread {



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
