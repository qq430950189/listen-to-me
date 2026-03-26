package com.github.listen_to_me.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 创作者财务概览VO
 */
@Data
@Schema(name = "FinanceSummaryVO", description = "创作者财务概览")
public class FinanceSummaryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "总收入")
    private BigDecimal totalIncome;

    @Schema(description = "可提现余额")
    private BigDecimal withdrawableBalance;

    @Schema(description = "冻结金额")
    private BigDecimal frozenBalance;

    @Schema(description = "已结算订单数")
    private Integer settledOrderCount;

    @Schema(description = "今日收入")
    private BigDecimal todayIncome;

    @Schema(description = "本月收入")
    private BigDecimal monthIncome;
}
