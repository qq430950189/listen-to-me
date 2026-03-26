package com.github.listen_to_me.common.config;

import com.github.listen_to_me.common.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security 7.x 核心配置类
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // 开启 @PreAuthorize 权限注解
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 配置密码加密器 (使用 BCrypt 强哈希算法)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 暴露 AuthenticationManager 实例，用于在 AuthController 中进行登录认证
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * 核心安全过滤链配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用 CSRF，因为我们的 Token 本身就能防御 CSRF，且前后端分离不需要它
                .csrf(AbstractHttpConfigurer::disable)

                // 2. 开启跨域支持（使用下方定义的 corsConfigurationSource）
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. 设置为无状态 Session，因为我们使用 JWT 进行认证，不依赖服务端的 Session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. 配置请求路径的权限规则
                .authorizeHttpRequests(auth -> auth
                        // ================== 公共白名单放行 ==================
                        // 放行登录、注册、验证码等认证接口（路径前缀为/api/common/auth）
                        .requestMatchers("/api/common/auth/**").permitAll()
                        // 放行文件上传等公共接口
                        .requestMatchers("/api/common/file/**").permitAll()
                        // 放行 Knife4J 和 Swagger OpenAPI3 的静态资源与接口数据
                        .requestMatchers(
                                "/doc.html",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/favicon.ico"
                        ).permitAll()
                        // 允许跨域的 OPTIONS 预检请求直接通过
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ================== 业务接口拦截 ==================
                        // 除了上述白名单，其余所有请求都必须携带有效 Token 才能访问
                        .anyRequest().authenticated()
                );

        // 5. 将自定义的 JWT 过滤器添加到 Spring Security 的 UsernamePasswordAuthenticationFilter 之前
        // 这样每次请求都会先解析 Token，如果 Token 有效，就将用户信息存入 SecurityContext
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 全局跨域 (CORS) 配置
     * 解决前端 Vue (如 http://localhost:5173) 访问后端 API 时的跨域拦截问题
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许所有来源（生产环境建议改为前端实际域名，如 "https://www.listentome.com"）
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // 允许所有请求头
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // 允许常见的 HTTP 方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        // 允许前端携带凭证（如 Cookie 或 Authorization 头）
        configuration.setAllowCredentials(true);
        // 预检请求的缓存时间（秒），减少 OPTIONS 请求频率
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有接口路径生效
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
