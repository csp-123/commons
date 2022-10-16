package com.commons.designmodes.strategy;

import java.math.BigDecimal;

/**
 * @description: 环境
 * @author: cccsp
 * @date: 2022/10/16 0:03
 */
public class StrategyContext {

    private Strategy strategy;

    public StrategyContext(Strategy strategy) {
        this.strategy = strategy;
    }

    public BigDecimal getResultMoney(BigDecimal money) {
        return strategy.acceptCash(money);
    }

}
