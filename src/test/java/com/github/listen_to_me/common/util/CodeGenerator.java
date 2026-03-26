package com.github.listen_to_me.common.util;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;

/**
 * MyBatis-Plus 代码自动生成器
 * 专为 listen_to_me 项目结构定制
 */
public class CodeGenerator {

    public static void main(String[] args) {
        // 1. 数据库连接配置 (请根据实际情况修改数据库名、账号和密码)
        String dbUrl = "jdbc:mysql://119.6.59.115:10041/listen-to-me?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
        String dbUser = "app";
        String dbPassword = "-j%Tic{50BxLC73";

        // 2. 获取项目根目录
        String projectPath = System.getProperty("user.dir");

        FastAutoGenerator.create(dbUrl, dbUser, dbPassword)
                // ================= 全局配置 =================
                .globalConfig(builder -> {
                    builder.author("ListenToMe Team") // 作者名称
                            .enableSpringdoc()        // 开启 OpenAPI 3 模式 (配合你 pom.xml 中的 knife4j-openapi3)
                            //.fileOverride()         // 全局覆盖已有文件的配置已失效，已迁移到策略配置中按模块启用.enableFileOverride()
                            .disableOpenDir()         // 生成后不自动打开文件夹
                            .outputDir(projectPath + "/src/main/java"); // Java 代码输出目录
                })

                // ================= 包配置 =================
                .packageConfig(builder -> {
                    builder.parent("com.github.listen_to_me") // 父包名
                            // 你的实体类放在了 domain.entity 下，这里专门指定
                            .entity("domain.entity")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            // 你的 controller 按角色划分了，这里生成的基础 controller 放在 base 下，避免污染你的业务 controller
                            .controller("controller.base")
                            // 强制将 Mapper XML 生成到 resources/mapper 目录下
                            .pathInfo(Collections.singletonMap(
                                    OutputFile.xml,
                                    projectPath + "/src/main/resources/mapper"
                            ));
                })

                // ================= 策略配置 =================
                .strategyConfig(builder -> {
                    // 添加你需要生成的所有表名
                    builder.addInclude(
                                    "sys_user", "sys_role", "sys_permission", "sys_user_role", "sys_role_permission", // RBAC
                                    "audio_info", "audio_transcript", // 音频
                                    "order_info", // 订单
                                    "sys_tag", "audio_tag_relation", "play_history", // 社交与标签
                                    "consult_slot" // 咨询
                            )
                            // 注意：根据你的目录结构，你的实体类叫 SysUser，说明你没有去掉 sys_ 前缀。
                            // 因此这里【不使用】 addTablePrefix("sys_")，保留原汁原味的表名映射。

                            // 1. 实体类策略
                            .entityBuilder()
                            .enableLombok() // 开启 Lombok
                            .enableTableFieldAnnotation() // 自动生成 @TableField 注解
                            .enableFileOverride() // <--- fileOverride()搬家到这里了：开启实体类覆盖
                            .logicDeleteColumnName("deleted") // 如果有逻辑删除字段，可以在这里指定（你的SQL暂无，可保留备用）

                            // 2. Mapper 策略
                            .mapperBuilder()
                            .enableMapperAnnotation() // 开启 @Mapper 注解
                            .enableBaseResultMap()    // XML 中生成 BaseResultMap
                            .enableBaseColumnList()   // XML 中生成 BaseColumnList
                            .enableFileOverride() // <--- fileOverride()搬家到这里了：开启实体类覆盖

                            // 3. Service 策略
                            .serviceBuilder()
                            .formatServiceFileName("I%sService") // 生成 IAudioInfoService 格式
                            .formatServiceImplFileName("%sServiceImpl")
                            .enableFileOverride() // <--- fileOverride()搬家到这里了：开启实体类覆盖

                            // 4. Controller 策略
                            .controllerBuilder()
                            .enableRestStyle() // 开启 @RestController
                            .enableFileOverride(); // <--- fileOverride()搬家到这里了：开启实体类覆盖
                })

                // ================= 模板引擎配置 =================
                .templateEngine(new VelocityTemplateEngine()) // 使用 Velocity
                .execute();

        System.out.println("🎉 代码生成完毕！请刷新项目目录。");
    }
}