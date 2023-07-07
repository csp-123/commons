package com.commons.blog.config;

import com.commons.core.config.BasePropertiesConfig;

/**
 * @description: 环境配置类
 * @author: cccsp
 * @date: 2022/9/19 15:13
 */
public class EnvPropertiesConfig extends BasePropertiesConfig {


    @Override
    public String[] getPropertiesFiles() {
        return new String[]{
                "classpath:config/blog-%s.properties"
        };
    }
}