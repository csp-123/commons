package com.commons.core.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.commons.core.pojo.BasePOJO;
import lombok.Getter;
import org.apache.ibatis.annotations.Mapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Title: Mybatis-plus 代码生成器
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2022/9/26 11:21
 */
@Getter
public class MPGenerator {

    private static final String MYSQL_PASSWORD = "root";

    private static final String MYSQL_USERNAME = "root";

    private static final String MYSQL_URL = "jdbc:mysql://120.48.35.213:3307/commons?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&serverTimezone=Asia/Shanghai";

    private static final String MYSQL_TABLE_PREFIX = "commons_";

    private static final String PACKAGE_PARENT = "com.commons";

    private static final String MODULE_NAME = "auth";

    private static final String PACKAGE_ENTITY = "pojo";



    public static void main(String[] args) {

        System.out.println("请输入模块名：");
        String moduleName = new Scanner(System.in).next();
        System.out.println("请输入表名，多个英文逗号分隔 所有输入 all：");
        String tableName = new Scanner(System.in).next();
        FastAutoGenerator.create(MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD)

                // 全局配置
                .globalConfig(builder -> {
                    builder
                            .outputDir(System.getProperty("user.dir") + "/" + moduleName + "/src/main/java") // 指定输出目录
                            .disableOpenDir();   //禁止打开输出目录，默认:true
                })

                // 包配置
                .packageConfig(builder -> {
                    builder.parent(PACKAGE_PARENT) // 设置父包名
                            .moduleName(MODULE_NAME) // 设置父包模块名
                            .entity(PACKAGE_ENTITY) // pojo 实体类包名,其它包名同理
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/" + moduleName +"/src/main/resources/mapper")); // 设置mapperXml生成路径
                })

                // 策略配置
                .strategyConfig(builder -> builder
                        .addInclude(getTables(tableName))
                        .addTablePrefix(MYSQL_TABLE_PREFIX) // 设置过滤表前缀
                        // entity 策略配置
                        .entityBuilder()
                        .enableLombok()
                        .enableChainModel()
                        //.logicDeleteColumnName("isDeleted") //逻辑删除字段名
                        .naming(NamingStrategy.underline_to_camel)  //数据库表映射到实体的命名策略：下划线转驼峰命
                        .columnNaming(NamingStrategy.underline_to_camel)    //数据库表字段映射到实体的命名策略：下划线转驼峰命
                        .addTableFills(
                                new Column("create_time", FieldFill.INSERT)
                        )   //添加表字段填充，"create_time"字段自动填充为插入时间，"modify_time"字段自动填充为插入修改时间
                        .enableTableFieldAnnotation()       // 开启生成实体时生成字段注解
                        .superClass(BasePOJO.class)
                        .enableFileOverride()


                        // mapper 策略配置
                        .mapperBuilder()
                        .superClass(BaseMapper.class)   //设置父类
                        .formatMapperFileName("%sMapper")   //格式化 mapper 文件名称
                        .mapperAnnotation(Mapper.class)       //开启 @Mapper 注解
                        .formatXmlFileName("%sXml") //格式化 Xml 文件名称 如 UserXml
                        .enableFileOverride()

                        // service 策略配置
                        .serviceBuilder()
                        .formatServiceFileName("%sService") // 如:UserService
                        .formatServiceImplFileName("%sServiceImpl") // 如:UserServiceImpl
                        .enableFileOverride()

                        // controller 策略配置
                        .controllerBuilder()
                        .formatFileName("%sController") // 如 UserController
                        .enableRestStyle() //开启生成 @RestController 控制器
                        .enableFileOverride()


                    )
                //.templateConfig(builder -> {
                //    // 实体类使用我们自定义模板 -- 模板位置
                //    builder.entity("templates/myEntity.java");
                //})
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }


    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}
