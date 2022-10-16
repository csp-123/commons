package com.commons.designmodes.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * @description: 返利模式
 * @author: cccsp
 * @date: 2022/10/16 0:23
 */
public class CashRebate extends Strategy{
    @Override
    public BigDecimal acceptCash(BigDecimal money) {
        // 返利收银模式，简单起见，返利 金额个位数数值,即 抹去个位数及小数点

        BigDecimal divide = money.divide(new BigDecimal(10), 0, RoundingMode.DOWN);
        return divide.multiply(new BigDecimal(10));

    }
}
