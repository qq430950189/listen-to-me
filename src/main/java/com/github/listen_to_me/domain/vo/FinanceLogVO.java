package com.github.listen_to_me.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 财务流水VO
 */
@Data
@Schema(name = "FinanceLogVO", description = "财务流水信息")
public class FinanceLogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "流水ID")
    private Long id;

    @Schema(description = "类型：income-收入，withdraw-提现")
    private String type;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "余额变动后余额")
    private BigDecimal balanceAfter;

    @Schema(description = "关联订单号")
    private String orderSn;

    @Schema(description = "关联音频标题")
    private String audioTitle;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
