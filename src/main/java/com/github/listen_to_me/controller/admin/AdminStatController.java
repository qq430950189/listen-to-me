package com.github.listen_to_me.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.listen_to_me.common.exception.BizException;
import com.github.listen_to_me.common.result.Result;
import com.github.listen_to_me.domain.entity.SysUser;
import com.github.listen_to_me.domain.query.StatQuery;
import com.github.listen_to_me.domain.vo.FinanceLogVO;
import com.github.listen_to_me.domain.vo.StatDashboardVO;
import com.github.listen_to_me.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 管理员端统计控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/stat")
@RequiredArgsConstructor
@Tag(name = "管理员端-统计接口", description = "数据统计、看板等接口")
public class AdminStatController {

    private final ISysUserService userService;

    /**
     * 全站收益统计
     */
    @GetMapping("/dashboard")
    @Operation(summary = "全站收益统计", description = "获取全站统计数据看板")
    public Result<StatDashboardVO> getStatDashboard(StatQuery query) {
        log.debug("查询全站统计看板");

        StatDashboardVO vo = new StatDashboardVO();
        vo.setTotalSales(BigDecimal.ZERO); // 实际项目中应计算
        vo.setTodaySales(BigDecimal.ZERO);
        vo.setActiveUsers(0);
        vo.setConversionRate(BigDecimal.ZERO);
        vo.setSalesTrend(new ArrayList<>());

        return Result.success(vo);
    }

    /**
     * 结算账单分页
     */
    @GetMapping("/finance/bill")
    @Operation(summary = "结算账单分页", description = "分页查询结算账单")
    public Result<Page<FinanceLogVO>> getFinanceBillPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.debug("查询结算账单分页");

        // 实际项目中应查询财务流水表
        Page<FinanceLogVO> page = new Page<>(pageNum, pageSize);
        page.setRecords(new ArrayList<>());
        page.setTotal(0);

        return Result.success(page);
    }
}
