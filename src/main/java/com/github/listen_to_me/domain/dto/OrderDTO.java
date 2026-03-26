package com.github.listen_to_me.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单请求DTO
 */
@Data
@Schema(name = "OrderDTO", description = "订单请求")
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "音频ID")
    @NotNull(message = "音频ID不能为空")
    private Long audioId;

    @Schema(description = "支付渠道：alipay, wechat", example = "alipay")
    private String payChannel;
}
