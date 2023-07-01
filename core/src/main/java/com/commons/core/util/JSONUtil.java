package com.commons.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Description:
 * JSON工具类
 * Project: commons
 * Author: chish
 * Create Time:2023/7/1 0:36
 */
public class JSONUtil {

    /**
     * JSON格式字段put操作，返回操作后的新的JSON字符串
     * @param sourceJsonStr 来源json串
     * @param targetField 目标字段
     * @param value 值
     * @return String
     */
    public static String put(String sourceJsonStr, String targetField, Object value) {
        JSONObject jsonObject = JSON.parseObject(sourceJsonStr);
        if (Objects.isNull(jsonObject)) {
            jsonObject = new JSONObject();
        }
        jsonObject.put(targetField, value);
        return JSON.toJSONString(jsonObject);
    }

    /**
     * JSON格式字段get操作，以指定类型返回指定字段
     * @param sourceJsonStr 来源json串
     * @param field 字段
     * @param clazz 类型
     * @return 值
     * @param <T> 返回值类型
     */
    public static <T> T get(String sourceJsonStr, String field, Class<T> clazz) {
        JSONObject jsonObject = JSON.parseObject(sourceJsonStr);
        if (Objects.isNull(jsonObject)) {
            return null;
        }
        return jsonObject.toJavaObject(clazz);
    }

    /**
     * 移除指定字段
     * @param sourceJsonStr 来源json串
     * @param field 字段
     * @return 操作后的json字符串
     */
    public static String remove(String sourceJsonStr, String field) {
        JSONObject jsonObject = JSON.parseObject(sourceJsonStr);
        if (Objects.isNull(jsonObject)) {
            return null;
        }
        jsonObject.remove(field);
        return JSON.toJSONString(jsonObject);
    }
}