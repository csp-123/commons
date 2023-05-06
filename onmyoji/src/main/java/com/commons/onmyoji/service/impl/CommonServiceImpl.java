package com.commons.onmyoji.service.impl;

import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.matcher.ImgMatcher;
import com.commons.onmyoji.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @description:
 * @author: cccsp
 * @date: 2023/2/23 23:17
 */
@Service
public class CommonServiceImpl implements CommonService {
    Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);


    @Override
    public boolean isInYardNow(Integer teamMemberCount) {

        String projectPath = System.getProperty("user.dir");
        String targetImgPath = projectPath + "/" + OnmyojiConstant.COMMON_IMG_PATH + OnmyojiConstant.YARD_LANTERN;

        if (teamMemberCount > 1) {
            // todo
            return ImgMatcher.match(ImgMatcher.getFullScreenShot(), targetImgPath, 1);
        } else {
            return ImgMatcher.match(targetImgPath, 1);
        }
    }

    @Override
    public void backToUpper(Integer teamMemberCount) {

        String projectPath = System.getProperty("user.dir");

        String path = "";

        boolean success = false;
        for (String backButton : OnmyojiConstant.BACK_BUTTONS) {
            path = projectPath + "\\" + OnmyojiConstant.COMMON_IMG_PATH + backButton;
            if (teamMemberCount > 1) {
                // todo
               success = ImgMatcher.match(ImgMatcher.getFullScreenShot(), path, 1);
            } else {
                success = ImgMatcher.match(path, 1);
            }

            if (success) {
                break;
            }
        }

        if (!success) {
            logger.error("返回上一级时发生异常,存在未匹配成功的界面");
            throw new RuntimeException();
        } else {
            ImgMatcher.matchAndClick(path, 1, true);
        }

        // 点击后等待0.5s 阴阳师需要加载
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void backToYard(Integer teamMemberCounts) {
        // 当前位置是否为庭院
        boolean inYard = isInYardNow(teamMemberCounts);

        if (inYard) {
            return;
        }

        // 返回至庭院
        do {
            // 返回上一层
            backToUpper(teamMemberCounts);
            // 判断当前是否在庭院
            inYard = isInYardNow(teamMemberCounts);
        } while (!inYard);
    }


}
