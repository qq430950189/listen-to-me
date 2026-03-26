package com.github.listen_to_me.controller.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.listen_to_me.common.constant.RoleConstant;
import com.github.listen_to_me.common.exception.BizException;
import com.github.listen_to_me.common.result.Result;
import com.github.listen_to_me.common.util.JwtUtils;
import com.github.listen_to_me.domain.dto.UserLoginDTO;
import com.github.listen_to_me.domain.dto.UserRegisterDTO;
import com.github.listen_to_me.domain.entity.SysUser;
import com.github.listen_to_me.domain.vo.LoginVO;
import com.github.listen_to_me.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/common/auth")
@RequiredArgsConstructor
@Tag(name = "认证接口", description = "登录、注册、Token刷新等接口")
public class AuthController {

    private final ISysUserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "通过用户名密码登录，返回JWT Token")
    public Result<LoginVO> saveLogin(@Valid @RequestBody UserLoginDTO dto) {
        log.debug("用户登录 - 账号: {}", dto.getUsername());

        // 1. 查询用户
        SysUser user = userService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, dto.getUsername()));

        if (user == null) {
            throw new BizException("用户名或密码错误");
        }

        // 2. 验证密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BizException("用户名或密码错误");
        }

        // 3. 生成Token
        String roleCode = user.getIsCreator() ? RoleConstant.ROLE_CREATOR : RoleConstant.ROLE_USER;
        String token = JwtUtils.generateToken(user.getId(), user.getUsername(), roleCode);

        // 4. 构建返回对象
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setExpireTime(JwtUtils.getExpiration(token));
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setIsCreator(user.getIsCreator());

        log.debug("用户登录成功 - 用户ID: {}", user.getId());
        return Result.success(vo);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "注册新用户账号")
    public Result<Void> saveRegister(@Valid @RequestBody UserRegisterDTO dto) {
        log.debug("用户注册 - 账号: {}", dto.getUsername());

        // 1. 检查用户名是否已存在
        long count = userService.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, dto.getUsername()));
        if (count > 0) {
            throw new BizException("用户名已存在");
        }

        // 2. 检查手机号是否已存在
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()) {
            count = userService.count(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getPhone, dto.getPhone()));
            if (count > 0) {
                throw new BizException("手机号已被注册");
            }
        }

        // 3. 创建用户
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname() != null ? dto.getNickname() : "听众_" + System.currentTimeMillis() % 10000);
        user.setPhone(dto.getPhone());
        user.setIsCreator(dto.getIsCreator() != null ? dto.getIsCreator() : false);
        user.setBalance(java.math.BigDecimal.ZERO);
        user.setFrozenBalance(java.math.BigDecimal.ZERO);
        user.setVersion(0);
        user.setCreateTime(LocalDateTime.now());

        userService.save(user);

        log.debug("用户注册成功 - 用户ID: {}", user.getId());
        return Result.success();
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新Token", description = "刷新JWT Token有效期")
    public Result<LoginVO> saveTokenRefresh(@RequestHeader("Authorization") String authHeader) {
        // 从Header中提取Token
        String token = authHeader.replace("Bearer ", "");

        // 验证Token
        if (!JwtUtils.validateToken(token)) {
            throw new BizException(401, "Token无效或已过期");
        }

        // 获取用户信息
        Long userId = JwtUtils.getUserId(token);
        String username = JwtUtils.getUsername(token);

        // 查询用户最新信息
        SysUser user = userService.getById(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }

        // 生成新Token
        String roleCode = user.getIsCreator() ? RoleConstant.ROLE_CREATOR : RoleConstant.ROLE_USER;
        String newToken = JwtUtils.generateToken(user.getId(), user.getUsername(), roleCode);

        // 构建返回对象
        LoginVO vo = new LoginVO();
        vo.setToken(newToken);
        vo.setExpireTime(JwtUtils.getExpiration(newToken));
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setIsCreator(user.getIsCreator());

        return Result.success(vo);
    }
}
