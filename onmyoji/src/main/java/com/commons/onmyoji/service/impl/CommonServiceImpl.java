package com.commons.onmyoji.service.impl;

import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.service.CommonService;
import com.commons.onmyoji.utils.FindRobot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: cccsp
 * @date: 2023/2/23 23:17
 */
@Service
public class CommonServiceImpl implements CommonService {
    Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Override
    public boolean isInYardNow() {

        String projectPath = System.getProperty("user.dir");
        FindRobot findRobot = new FindRobot(projectPath + "/" + OnmyojiConstant.COMMON_IMG_PATH + OnmyojiConstant.YARD_LANTERN, null, 0, 0);

        return findRobot.isFound();
    }

    @Override
    public void backToUpper() {

        String projectPath = System.getProperty("user.dir");


        boolean success = false;
        for (String backButton : OnmyojiConstant.BACK_BUTTONS) {
            String path = projectPath + "\\" + OnmyojiConstant.COMMON_IMG_PATH + backButton;
            success = FindRobot.touchPic(path);
            if (success) {
                break;
            }
        }

        if (!success) {
            logger.error("返回上一级时发生异常,存在未匹配成功的界面");
            throw new RuntimeException();
        }


    }


}
