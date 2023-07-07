package com.commons.blog.utils;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;

/**
 * 参数校验工具类
 *
 * @author chishupeng
 * @date 2023/7/6 11:12 AM
 */
public class ParamAssertUtil {

    /**
     * 校验参数不能为空
     *
     * @param param 参数
     * @param paramName 参数名称
     */
    public static void assertNotNull(Object param, String paramName) {
        if (!StringUtils.hasText(paramName))  {
            paramName = "数据";
        }
        Assert.isTrue(isNotEmpty(param), String.format("%s为空", paramName));
    }

    /**
     * 校验相等
     * @param obj1 参数1
     * @param obj2 参数2
     * @param msg 提示信息
     */
    public static void assertEqual(Object obj1, Object obj2, String msg) {
        Assert.isTrue(Objects.equals(obj1, obj2), msg);
    }


    private static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return ((String) obj).isEmpty();
        } else if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        return false;
    }


    private static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
}
