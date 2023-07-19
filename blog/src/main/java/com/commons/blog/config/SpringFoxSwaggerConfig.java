package com.commons.blog.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * Swagger配置
 *
 * @author chishupeng
 * @date 2023/7/3 3:09 PM
 */
@Slf4j
@Configuration
@EnableOpenApi
public class SpringFoxSwaggerConfig implements ApplicationListener<WebServerInitializedEvent> {

    /**
     * 配置基本信息
     * @return
     */
    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("个人网站系统")
                .description("")
                .termsOfServiceUrl("")
                .contact(new Contact("csp","","chishup@163.com"))
                .version("1.0")
                .build();
    }


    /**
     * 配置文档生成最佳实践
     * @param apiInfo
     * @return
     */
    @Bean
    public Docket createRestApi(ApiInfo apiInfo) {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo)
                .groupName("网站")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        try {
            //获取IP
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            //获取端口号
            int port = event.getWebServer().getPort();
            //获取应用名
            String applicationName = event.getApplicationContext().getApplicationName();
            log.info("项目启动启动成功！接口文档地址: http://"+hostAddress+":"+event.getWebServer().getPort()+applicationName+"/swagger-ui/index.html");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }



}
