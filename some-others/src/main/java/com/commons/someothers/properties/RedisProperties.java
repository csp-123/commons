package com.commons.someothers.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Title:
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2022/10/10 10:34
 */
@Component
@ConfigurationProperties(prefix = "spring.redis")
@Getter
@Setter
public class RedisProperties {

    private String port;

    private String host;

    private String database;

    private String password;

}
