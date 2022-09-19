package com.commons.core.config;

/**
 * @description: 环境配置类
 * @author: cccsp
 * @date: 2022/9/19 15:13
 */
public class EnvPropertiesConfig extends BasePropertiesConfig {


    @Override
    public String[] getPropertiesFiles() {
        return new String[]{
                "classpath:config/commons-%s.properties"
        };
    }
}