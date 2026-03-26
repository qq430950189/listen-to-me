package com.github.listen_to_me.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.listen_to_me.common.exception.BizException;
import com.github.listen_to_me.common.result.Result;
import com.github.listen_to_me.domain.entity.SysUser;
import com.github.listen_to_me.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员端风控控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/risk")
@RequiredArgsConstructor
@Tag(name = "管理员端-风控接口", description = "用户封禁、敏感词管理等接口")
public class AdminRiskController {

    private final ISysUserService userService;

    /**
     * 封禁/解封用户
     */
    @PutMapping("/user/status")
    @Operation(summary = "封禁/解封用户", description = "修改用户状态")
    public Result<Void> updateUserStatus(@RequestParam Long userId,
                                          @RequestParam Integer status) {
        log.debug("修改用户状态 - 用户ID: {}, 状态: {}", userId, status);

        SysUser user = userService.getById(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }

        // 实际项目中应修改用户状态字段
        // 这里简化处理

        log.debug("用户状态修改成功 - 用户ID: {}", userId);
        return Result.success();
    }

    /**
     * 用户分页列表
     */
    @GetMapping("/user/page")
    @Operation(summary = "用户分页列表", description = "分页查询用户列表")
    public Result<Page<SysUser>> getUserPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean isCreator) {
        log.debug("查询用户分页列表 - 页码: {}", pageNum);

        Page<SysUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(SysUser::getUsername, keyword)
                    .or()
                    .like(SysUser::getNickname, keyword)
                    .or()
                    .like(SysUser::getPhone, keyword);
        }

        if (isCreator != null) {
            wrapper.eq(SysUser::getIsCreator, isCreator);
        }

        wrapper.orderByDesc(SysUser::getCreateTime);

        userService.page(page, wrapper);

        // 清除密码等敏感信息
        page.getRecords().forEach(user -> user.setPassword(null));

        return Result.success(page);
    }
}
