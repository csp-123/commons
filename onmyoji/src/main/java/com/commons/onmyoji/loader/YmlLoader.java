package com.commons.onmyoji.loader;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Title:
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/20 16:29
 */
@Component
public class YmlLoader<T> implements EnvironmentAware {

    private static Logger logger = LoggerFactory.getLogger(YmlLoader.class);


    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment, "environment must be instance of ConfigurableEnvironment.");
        this.environment = environment;
    }


    public List<PropertiesPropertySource> loadAllYml(String regex) throws IOException {
        List<PropertiesPropertySource> result = Lists.newArrayList();
        Map<String, Resource> ymlPropertySourceMap = new LinkedHashMap<>();

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resourceArr = resourcePatternResolver.getResources(regex);
        Arrays.stream(resourceArr).forEach(resource -> {
            try {
                if (resource.getInputStream() != null) {
                    ymlPropertySourceMap.put(resource.getFilename(), resource);
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        });

        if (!ymlPropertySourceMap.isEmpty()) {
            ymlPropertySourceMap.forEach((name, resource) -> {
                YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
                yamlPropertiesFactoryBean.setResources(resource);
                PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource(name, yamlPropertiesFactoryBean.getObject());
                ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) environment;
                MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
                propertySources.addLast(propertiesPropertySource);
                result.add(propertiesPropertySource);
            });
        }
        return result;
    }


    public List<T> loadAs(String regex, Class<T> type) throws IOException {
        List<T> result = Lists.newArrayList();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resourceArr = resourcePatternResolver.getResources(regex);
        Arrays.stream(resourceArr).forEach(resource -> {
            try {
                resource.getInputStream();
                Yaml yaml = new Yaml();
                T t = yaml.loadAs(resource.getInputStream(), type);
                result.add(t);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        });

        return result;
    }



}
