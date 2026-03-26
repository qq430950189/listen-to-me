package com.github.listen_to_me.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单支付VO
 */
@Data
@Schema(name = "OrderPayVO", description = "订单支付信息")
public class OrderPayVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单号")
    private String orderSn;

    @Schema(description = "支付金额")
    private BigDecimal payAmount;

    @Schema(description = "支付状态：0-待支付，1-已支付，2-已取消")
    private Integer payStatus;

    @Schema(description = "支付URL")
    private String payUrl;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;
}
