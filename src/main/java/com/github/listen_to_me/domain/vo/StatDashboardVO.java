package com.github.listen_to_me.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 统计看板VO
 */
@Data
@Schema(name = "StatDashboardVO", description = "统计看板信息")
public class StatDashboardVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "总销售额")
    private BigDecimal totalSales;

    @Schema(description = "今日销售额")
    private BigDecimal todaySales;

    @Schema(description = "活跃用户数")
    private Integer activeUsers;

    @Schema(description = "付费转化率")
    private BigDecimal conversionRate;

    @Schema(description = "销售额趋势")
    private List<SalesTrendVO> salesTrend;

    /**
     * 销售额趋势VO
     */
    @Data
    @Schema(name = "SalesTrendVO", description = "销售额趋势")
    public static class SalesTrendVO implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "日期")
        private String date;

        @Schema(description = "金额")
        private BigDecimal amount;
    }
}
