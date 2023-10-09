package com.commons.onmyoji.service.impl;

import com.commons.onmyoji.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
        return false;
    }

    @Override
    public void backToUpper(Integer teamMemberCount) {


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
