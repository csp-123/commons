package com.commons.onmyoji.job;

import com.alibaba.fastjson.JSON;
import com.commons.onmyoji.config.OnmyojiScriptConfig;
import com.commons.onmyoji.loader.YmlLoader;
import com.commons.onmyoji.producer.InstanceZoneProducer;
import com.google.common.collect.Lists;
import javassist.Modifier;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Title:
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/20 16:31
 */
@Component
public class JobLoader {

    private static final Logger logger = LoggerFactory.getLogger(JobLoader.class);

    private final Map<String, InstanceZoneProducer> producerMap;

    private Map<String, OnmyojiJob> jobs;

    private final YmlLoader ymlLoader;


    public JobLoader(Map<String, InstanceZoneProducer> producerMap, YmlLoader ymlLoader) {
        this.producerMap = producerMap;
        this.ymlLoader = ymlLoader;
    }

    @SneakyThrows
    public Map<String, OnmyojiJob> loadAllJobs() {
        logger.info("==============开始加载配置=============");
        if (jobs == null){
            List<PropertiesPropertySource> sources = ymlLoader.loadAllYml("classpath:job/*.yml");
            jobs = sources.stream().map(this::parseJob).collect(Collectors.toMap(r -> r.getId(), r -> r));
        }
        logger.info("==============加载配置完毕=============");

        return jobs;
    }


    private OnmyojiJob parseJob(PropertiesPropertySource source) {

        OnmyojiJob job = new OnmyojiJob();
        job.setId(source.getName().replace(".yml", ""));
        job.setName((String) source.getProperty("name"));
        job.setTeamType((Integer) source.getProperty("teamType"));
        job.setTeamMembers((Integer) source.getProperty("teamMembers"));
        job.setHangUpType(buildHangUpType(source));
        logger.info("开始加载任务文件："  + job.getId());
        logger.info("任务名称：" + job.getName());
        InstanceZoneProducer producer = producerMap.get(source.getProperty("producerBean"));
        Assert.notNull(producer, "未找到producerBean：" + source.getProperty("producerBean"));
        job.setProducer(producer);
        logger.info("处理器：" + producer.getProcuderName() + " 加载成功！ ");
        job.setConfig(buildConfig(source, producer));
        logger.info(JSON.toJSONString(job));
        return job;
    }

    private HangUpType buildHangUpType(PropertiesPropertySource source) {
        HangUpType hangUpType = new HangUpType();
        source.getSource().entrySet().stream()
                .filter(s -> s.getKey().startsWith("hangUpType"))
                .forEach(e -> {
                    try {
                        setValue(hangUpType, e.getKey().replace("hangUpType.", ""), e.getValue());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
        return hangUpType;
    }

    @SneakyThrows
    private OnmyojiScriptConfig buildConfig(PropertiesPropertySource source, InstanceZoneProducer producer) {
        Method declaredMethod = producer.getClass().getDeclaredMethod("produce", OnmyojiJob.class);
        Type[] parameterTypes = declaredMethod.getGenericParameterTypes();
        ParameterizedType parameterizedType = (ParameterizedType) parameterTypes[0];
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        OnmyojiScriptConfig config = (OnmyojiScriptConfig) ((Class)actualTypeArguments[0]).newInstance();



        source.getSource().entrySet().stream()
                .filter(s -> s.getKey().startsWith("config"))
                .forEach(e -> {
                    try {
                        setValue(config, e.getKey().replace("config.", ""), e.getValue());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });

        return config;
    }


    private void setValue(Object object, String key, Object value) throws IllegalAccessException, InstantiationException {
        if (StringUtils.isEmpty(key)) {
            return;
        }

        String[] arr = key.split("\\.");

        Object parent = object;
        Object sub = null;
        for (int i = 0; i < arr.length; i++) {
            if (i != 0) {
                parent = sub;
            }

            String fieldName = arr[i];
            Pattern pattern = Pattern.compile("\\[(([0-9]+))\\]");

            Matcher matcher = pattern.matcher(fieldName);
            boolean isArray = matcher.find();
            if (isArray) {
                fieldName = fieldName.replace(matcher.group(0), "");
            }

            Field field = getField(parent.getClass(), fieldName);
            if (field == null) {
                throw new RuntimeException("无法将字段：" + fieldName + "，映射到类型：" + parent.getClass());
            }

            field.setAccessible(true);
            if (isArray) {
                List list = (List) field.get(parent);
                if (list == null) {
                    list = (List) genValue(field.getType());
                    setValueToField(parent, field, list);
                }
                int index = Integer.valueOf(matcher.group(1));
                //扩容
                if (list.size() < index + 1) {
                    int size = list.size();
                    for (int j = 0; j < index + 1 - size; j++) {
                        list.add(null);
                    }
                }

                if (i == arr.length - 1){
                    list.set(index, value);
                } else {
                    if (list.get(index) != null) {
                        sub = list.get(index);
                    } else {
                        sub = genValue(getParameterizedType(field)[0]);
                        list.set(index, sub);
                    }
                }

            } else {
                if (i == arr.length - 1) {
                    setValueToField(parent, field, transType(field.getType(), value));
                } else {
                    Object o = field.get(parent);
                    if (o == null){
                        sub = genValue(field.getType());
                        setValueToField(parent, field, sub);
                    } else {
                        sub = o;
                    }

                }
            }
        }


    }
    private Field getField(Class clz, String fieldName){
        List<Field> fieldList = getAllFields(clz,null);
        for (Field field : fieldList) {
            if(field.getName().equals(fieldName)){
                return field;
            }
        }
        return null;
    }

    private List<Field> getAllFields(Class<?> clz, List<Field> fieldList){
        if(clz==null || clz.getName().equals(Object.class.getName())){
            return fieldList;
        }
        if(fieldList==null){
            fieldList = new ArrayList<Field>();
        }
        Field[] fields = clz.getDeclaredFields();
        if(fields!=null && fields.length>0){
            for (Field field : fields) {
                //添加非静态的字段
                //todo 跳过子类复写的字段
                if(!Modifier.isStatic(field.getModifiers())){
                    fieldList.add(field);
                }
            }
        }
        return getAllFields(clz.getSuperclass(),fieldList);
    }


    private  Object genValue(Class<? extends Object> clazz) throws IllegalAccessException, InstantiationException {
        Object res;
        if (List.class.isAssignableFrom(clazz)) {
            res = Lists.newArrayList();
        }else if (MultiValueMap.class.isAssignableFrom(clazz)) {
            res = new LinkedMultiValueMap<>();
        } else {
            res = clazz.newInstance();
        }
        return res;
    }

    private void setValueToField(Object target, Field field, Object value) throws IllegalAccessException {
        field.set(target, value);
    }


    private Class<?>[] getParameterizedType(Field f) {


        Type fc = f.getGenericType();
        if (fc != null && fc instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) fc;


            Type[] types = pt.getActualTypeArguments();

            if (types != null && types.length > 0) {
                Class<?>[] classes = new Class<?>[types.length];
                for (int i = 0; i < classes.length; i++) {
                    classes[i] = (Class<?>) types[i];
                }
                return classes;
            }
        }
        return null;
    }


    public static Object transType(Class type, Object value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (type == null || value == null) {
            return value;
        }

        if (type.isAssignableFrom(value.getClass())) {
            return value;
        }

        if (StringUtils.isEmpty(String.valueOf(value).trim())) {
            return null;
        }

        if (type == String.class) {
            return String.valueOf(value);
        }

        if (type.isEnum()) {
            Object[] enums = type.getEnumConstants();
            for (Object enumObj : enums) {
                if (String.valueOf(value).equals(String.valueOf(enumObj))) {
                    return enumObj;
                }
            }
        }

        if (type == Float.class) {
            return Float.parseFloat(String.valueOf(value));
        }

        if (type == Double.class) {
            return Double.parseDouble(String.valueOf(value));
        }

        if (type == Integer.class) {
            if (String.valueOf(value).contains(".")) {
                Double d = Double.parseDouble(String.valueOf(value));
                return d.intValue();
            } else {
                return Integer.parseInt(String.valueOf(value));
            }
        }
        if (type == Short.class) {
            if (String.valueOf(value).contains(".")) {
                Double d = Double.parseDouble(String.valueOf(value));
                return d.shortValue();
            } else {
                return Short.parseShort(String.valueOf(value));
            }
        }
        if (type == Boolean.class) {
            if (String.valueOf(value).equalsIgnoreCase("TRUE")
                    || String.valueOf(value).equalsIgnoreCase("1")
            ) {
                return true;
            }
            if (String.valueOf(value).equalsIgnoreCase("FALSE")
                    || String.valueOf(value).equalsIgnoreCase("0")
            ) {
                return false;
            }
            return null;
        }

        if (type == Date.class) {
            return sdf.format(String.valueOf(value));
        }
        if (type == Long.class) {
            if (String.valueOf(value).contains(".")) {
                Double d = Double.parseDouble(String.valueOf(value));
                return d.longValue();
            } else {
                return Long.parseLong(String.valueOf(value));
            }
        }
        return value;
    }
}
