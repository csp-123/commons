package com.commons.onmyoji.utils;

/**
 * @description: 通用工具类
 * @author: cccsp
 * @date: 2023/2/23 23:47
 */
public class CommonKit {

    public static int getRandomDelay() {
        long round = Math.round(Math.random());

        return (int) round * 100;
    }

}
