package com.commons.onmyoji.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

/**
 * @author chishupeng
 * @date 2023/8/23 10:51 AM
 */
@ConfigurationProperties(prefix = "onmyoji")
@Configuration
@Getter
@Setter
public class OnmyojiConfig {

    private Double threshold;


}
