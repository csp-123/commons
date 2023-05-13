package com.commons.onmyoji.utils;

import com.commons.onmyoji.constant.OnmyojiConstant;
import com.commons.onmyoji.job.OnmyojiJob;
import com.commons.onmyoji.matcher.ImgMatcher;
import com.commons.onmyoji.matcher.ImgMatcher2;
import com.commons.onmyoji.matcher.Matcher;
import com.commons.onmyoji.thread.OnmyojiDeamonThread;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
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


        String path = "D:\\IdeaProjects\\onmyoji\\src\\main\\resources\\img\\tansuo\\challengeBoss.png";

        Matcher matcher = new Matcher(1920, 1080);

//        matcher.count(path);

        matcher.count(path,  false);


    }
}
