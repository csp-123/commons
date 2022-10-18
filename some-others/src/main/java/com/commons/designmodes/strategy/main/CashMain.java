package com.commons.designmodes.strategy;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * @description: 收银业务类
 * @author: cccsp
 * @date: 2022/10/16 0:39
 */
public class CashMain {

    public static void main(String[] args) {
        StrategyContext context;
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入会员账号：");
        String account = scanner.next();
        // 打折收费
        if (account.contains("csp")) {
            context = new StrategyContext(new CashDiscount());
        }
        // 返利收费
        else if (account.contains("17561168557")) {
            context = new StrategyContext(new CashRebate());
        }
        // 普通收费
        else {
            context = new StrategyContext(new CashNormal());
        }

        System.out.println(context.getResultMoney(new BigDecimal("178.50")));



    }
}
