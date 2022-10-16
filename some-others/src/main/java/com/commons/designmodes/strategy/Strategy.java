package com.commons.designmodes.strategy;

import java.math.BigDecimal;

/**
 * @description: 策略抽象类
 *      acceptCash : 收银方法
 * @author: cccsp
 * @date: 2022/10/16 0:01
 */
public abstract class Strategy {

    public abstract BigDecimal acceptCash(BigDecimal money);
}
