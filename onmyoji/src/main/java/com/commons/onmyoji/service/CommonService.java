package com.commons.onmyoji.service;

import com.commons.onmyoji.utils.FindRobot;

/**
 * @description: 通用服务
 * @author: cccsp
 * @date: 2023/2/23 23:16
 */
public interface CommonService {
    /**
     * 当前是否位于庭院
     * @return
     */
    boolean isInYardNow();

    /**
     *  返回上层界面 (阴阳师中含两种返回按钮)
     */
    void backToUpper();


}
