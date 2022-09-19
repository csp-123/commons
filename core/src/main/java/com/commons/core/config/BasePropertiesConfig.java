package com.commons.core.config;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.util.Assert;

/**
 * @description: BasePropertiesConfig
 * @author: cccsp
 * @date: 2022/9/19 15:14
 */
public abstract class BasePropertiesConfig implements EnvironmentPostProcessor {

    public abstract String[] getPropertiesFiles();
    

    @SneakyThrows
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String env = environment.getProperty("spring.profiles.active");
        Assert.notNull(env, "配置文件初始化，获取当前环境失败，请确认已配置：spring.profiles.active");
        for (String location : getPropertiesFiles()) {
            environment.getPropertySources().addFirst(new ResourcePropertySource(String.format(location, env)));
        }
    }
}