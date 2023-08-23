package com.commons.onmyoji.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

/**
 * @author chishupeng
 * @date 2023/8/23 11:40 AM
 */
@Configuration
public class Config {

    @Bean
    public Robot getRobot() {
        try {
            return new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
}
