package com.github.listen_to_me.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情VO
 */
@Data
@Schema(name = "OrderDetailVO", description = "订单详情信息")
public class OrderDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单号")
    private String orderSn;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "音频ID")
    private Long audioId;

    @Schema(description = "音频标题")
    private String audioTitle;

    @Schema(description = "音频封面")
    private String audioCover;

    @Schema(description = "支付金额")
    private BigDecimal payAmount;

    @Schema(description = "支付状态：0-待支付，1-已支付，2-已取消")
    private Integer payStatus;

    @Schema(description = "支付渠道")
    private String payChannel;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
