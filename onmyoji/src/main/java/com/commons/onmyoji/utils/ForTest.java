package com.commons.onmyoji.utils;

import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.job.OnmyojiJob;
import com.google.common.collect.Maps;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @description: 测试
 * @author: cccsp
 * @date: 2023/2/23 17:08
 */
public class ForTest {
    public static void main(String[] args) {

        String projectPath = System.getProperty("user.dir");
        FindRobot findRobot = new FindRobot("D:\\java\\JavaProjects\\commons\\onmyoji\\src\\main\\resources\\img\\royalsoul\\team.png", null, 0, 0);
        System.out.println(findRobot.getFindX());
        System.out.println(findRobot.getFindY());
        OnmyojiJob job = new OnmyojiJob();
        ConcurrentHashMap map = new ConcurrentHashMap();
        StringBuilder sb = new StringBuilder();
        String s = "a;";
        map.put("a", "a");
        ThreadLocal tl = new ThreadLocal();
        tl.set("s");


    }
}
