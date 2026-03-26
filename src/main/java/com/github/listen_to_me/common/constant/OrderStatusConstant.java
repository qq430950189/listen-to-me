package com.github.listen_to_me.common.constant;

/**
 * 订单状态常量
 */
public interface OrderStatusConstant {

    /**
     * 待支付
     */
    int PAY_STATUS_PENDING = 0;

    /**
     * 已支付
     */
    int PAY_STATUS_PAID = 1;

    /**
     * 已取消
     */
    int PAY_STATUS_CANCELLED = 2;
}
