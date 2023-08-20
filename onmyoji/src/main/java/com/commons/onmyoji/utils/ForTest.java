package com.commons.onmyoji.utils;

import lombok.SneakyThrows;

/**
 * @description: 测试
 * @author: cccsp
 * @date: 2023/2/23 17:08
 */
public class ForTest {


    @SneakyThrows
    public static void main(String[] args) {


        String s = "sdada asdasda,sdadsa ,dsada";

        String[] split = s.split("[ ,]");

        for (String s1 : split) {
            System.out.println(s1);
        }


    }
}
