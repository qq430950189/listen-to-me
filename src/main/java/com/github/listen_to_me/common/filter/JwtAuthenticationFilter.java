package com.github.listen_to_me.common.filter;

import com.github.listen_to_me.common.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 认证过滤器
 * 拦截所有请求，解析 Token 并构建 Authentication 对象存入 SecurityContext
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            // 1. 从请求头获取 Token
            String token = resolveToken(request);

            // 2. 验证 Token 并设置认证信息
            if (StringUtils.hasText(token) && JwtUtils.validateToken(token)) {
                // 从 Token 中获取用户信息
                Long userId = JwtUtils.getUserId(token);
                String username = JwtUtils.getUsername(token);
                String roleCode = JwtUtils.getRoleCode(token);

                if (userId != null && username != null) {
                    // 构建权限列表
                    SimpleGrantedAuthority authority = roleCode != null
                            ? new SimpleGrantedAuthority(roleCode)
                            : new SimpleGrantedAuthority("ROLE_USER");

                    // 创建认证对象
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userId,
                                    null,
                                    Collections.singletonList(authority)
                            );

                    // 存入 SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.debug("JWT 认证成功 - 用户ID: {}, 用户名: {}, 角色: {}", userId, username, roleCode);
                }
            }
        } catch (Exception e) {
            log.debug("JWT 认证失败: {}", e.getMessage());
            // 清除认证信息
            SecurityContextHolder.clearContext();
        }

        // 3. 继续过滤链
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中解析 Token
     *
     * @param request HTTP 请求
     * @return JWT Token，如果不存在则返回 null
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
