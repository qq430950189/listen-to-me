package com.github.listen_to_me.common.constant;

/**
 * 咨询时间槽状态常量
 */
public interface SlotStatusConstant {

    /**
     * 可选
     */
    int STATUS_AVAILABLE = 0;

    /**
     * 锁定（下单中）
     */
    int STATUS_LOCKED = 1;

    /**
     * 已约
     */
    int STATUS_BOOKED = 2;

    /**
     * 完成
     */
    int STATUS_FINISHED = 3;
}
