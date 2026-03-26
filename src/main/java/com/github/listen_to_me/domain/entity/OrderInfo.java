package com.github.listen_to_me.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author ListenToMe Team
 * @since 2026-03-25
 */
@Getter
@Setter
@TableName("order_info")
@Schema(name = "OrderInfo", description = "")
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_sn")
    private String orderSn;

    @TableField("user_id")
    private Long userId;

    @TableField("audio_id")
    private Long audioId;

    @TableField("pay_amount")
    private BigDecimal payAmount;

    @Schema(description = "0-待支付, 1-已支付, 2-已取消")
    @TableField("pay_status")
    private Integer payStatus;

    @Schema(description = "alipay, wechat")
    @TableField("pay_channel")
    private String payChannel;

    @TableField("pay_time")
    private LocalDateTime payTime;

    @TableField("create_time")
    private LocalDateTime createTime;
}
