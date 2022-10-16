package com.commons.designmodes.strategy;

import java.math.BigDecimal;

/**
 * @description: 打折收费
 * @author: cccsp
 * @date: 2022/10/16 0:20
 */
public class CashDiscount extends Strategy{
    @Override
    public BigDecimal acceptCash(BigDecimal money) {
        // 简单起见，固定八折
        System.out.println("打折收银模式，金额进行打折处理");
        return money.multiply(new BigDecimal("0.8"));
    }
}
