package com.commons.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @description: Core自动装配配置
 * @author: cccsp
 * @date: 2022/9/19 15:14
 */
@Configuration
@ComponentScan(basePackages = {"com.commons.core.*"})
public class CoreAutoConfiguration {
}
