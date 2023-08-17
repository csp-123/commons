package com.commons.onmyoji.utils;

import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.entity.ScreenSnapshot;
import com.commons.onmyoji.job.OnmyojiJob;
import com.commons.onmyoji.matcher.ImgMatcher;
import com.commons.onmyoji.matcher.ImgMatcher2;
import com.commons.onmyoji.matcher.Matcher;
import com.commons.onmyoji.thread.OnmyojiDeamonThread;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.awt.image.BufferedImage;
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


    @SneakyThrows
    public static void main(String[] args) {


        String s = "sdada asdasda,sdadsa ,dsada";

        String[] split = s.split("[ ,]");

        for (String s1 : split) {
            System.out.println(s1);
        }


    }
}
