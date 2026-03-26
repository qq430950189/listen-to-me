package com.github.listen_to_me.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 提现请求DTO
 */
@Data
@Schema(name = "WithdrawDTO", description = "提现请求")
public class WithdrawDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "提现金额")
    @NotNull(message = "提现金额不能为空")
    @DecimalMin(value = "10.00", message = "最低提现金额为10元")
    private BigDecimal amount;

    @Schema(description = "提现渠道：alipay, wechat, bank", example = "alipay")
    private String channel;

    @Schema(description = "收款账号")
    private String account;
}
