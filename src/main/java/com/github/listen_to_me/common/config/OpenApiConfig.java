package com.github.listen_to_me.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /**
     * 配置全局 API 基础信息
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ListenToMe (听我说) - 核心API接口文档")
                        .version("1.0.0")
                        .description("ListenToMe 平台的接口文档，包含听众、创作者、后台管理端API。")
                        .contact(new Contact().name("ListenToMe Team").email("contact@listentome.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

    /**
     * 分组1：听众端接口
     */
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("1. 听众端接口")
                .pathsToMatch("/api/user/**")
                .build();
    }

    /**
     * 分组2：创作者端接口
     */
    @Bean
    public GroupedOpenApi creatorApi() {
        return GroupedOpenApi.builder()
                .group("2. 创作者端接口")
                .pathsToMatch("/api/creator/**")
                .build();
    }

    /**
     * 分组3：管理端接口
     */
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("3. 管理端接口")
                .pathsToMatch("/api/admin/**")
                .build();
    }

    /**
     * 分组4：公共与认证接口
     */
    @Bean
    public GroupedOpenApi commonApi() {
        return GroupedOpenApi.builder()
                .group("4. 公共接口")
                .pathsToMatch("/api/common/**", "/auth/**")
                .build();
    }
}
