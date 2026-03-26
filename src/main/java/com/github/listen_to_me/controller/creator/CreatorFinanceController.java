package com.github.listen_to_me.controller.creator;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.listen_to_me.common.exception.BizException;
import com.github.listen_to_me.common.result.Result;
import com.github.listen_to_me.domain.dto.WithdrawDTO;
import com.github.listen_to_me.domain.entity.SysUser;
import com.github.listen_to_me.domain.query.FinanceQuery;
import com.github.listen_to_me.domain.vo.FinanceLogVO;
import com.github.listen_to_me.domain.vo.FinanceSummaryVO;
import com.github.listen_to_me.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 创作者端财务控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/creator/finance")
@RequiredArgsConstructor
@Tag(name = "创作者端-财务接口", description = "收益统计、提现等接口")
public class CreatorFinanceController {

    private final ISysUserService userService;

    /**
     * 收益概览
     */
    @GetMapping("/summary")
    @Operation(summary = "收益概览", description = "获取创作者收益统计")
    public Result<FinanceSummaryVO> getFinanceSummary(@RequestHeader("X-User-Id") Long userId) {
        log.debug("查询收益概览 - 创作者ID: {}", userId);

        SysUser user = userService.getById(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }

        FinanceSummaryVO vo = new FinanceSummaryVO();
        vo.setTotalIncome(BigDecimal.ZERO); // 实际项目中应计算
        vo.setWithdrawableBalance(user.getBalance());
        vo.setFrozenBalance(user.getFrozenBalance());
        vo.setSettledOrderCount(0);
        vo.setTodayIncome(BigDecimal.ZERO);
        vo.setMonthIncome(BigDecimal.ZERO);

        return Result.success(vo);
    }

    /**
     * 账单流水
     */
    @GetMapping("/log")
    @Operation(summary = "账单流水", description = "分页查询财务流水")
    public Result<Page<FinanceLogVO>> getFinanceLogPage(FinanceQuery query,
                                                         @RequestHeader("X-User-Id") Long userId) {
        log.debug("查询账单流水 - 创作者ID: {}", userId);

        // 实际项目中应查询财务流水表
        Page<FinanceLogVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        page.setRecords(new ArrayList<>());
        page.setTotal(0);

        return Result.success(page);
    }

    /**
     * 申请提现
     */
    @PostMapping("/withdraw")
    @Operation(summary = "申请提现", description = "申请提现余额")
    public Result<Void> saveWithdraw(@RequestBody WithdrawDTO dto,
                                      @RequestHeader("X-User-Id") Long userId) {
        log.debug("申请提现 - 创作者ID: {}, 金额: {}", userId, dto.getAmount());

        SysUser user = userService.getById(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }

        // 检查余额
        if (user.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new BizException("余额不足");
        }

        // 扣减余额
        user.setBalance(user.getBalance().subtract(dto.getAmount()));
        userService.updateById(user);

        log.debug("提现申请成功 - 金额: {}", dto.getAmount());
        return Result.success();
    }
}
