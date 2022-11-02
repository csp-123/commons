package com.commons.designmodes.strategy;

import java.math.BigDecimal;

/**
 * @description: 正常价格收银
 * @author: cccsp
 * @date: 2022/10/16 0:18
 */
public class CashNormal extends Strategy{


    @Override
    public BigDecimal acceptCash(BigDecimal money) {
        System.out.println("正常收银模式，金额不做变动！");
        return money;
    }
}
