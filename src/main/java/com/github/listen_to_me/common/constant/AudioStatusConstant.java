package com.github.listen_to_me.common.constant;

/**
 * 音频状态常量
 */
public interface AudioStatusConstant {

    /**
     * 草稿/下架
     */
    int STATUS_DRAFT = 0;

    /**
     * 转码中
     */
    int STATUS_TRANSCODING = 1;

    /**
     * 已发布
     */
    int STATUS_PUBLISHED = 2;

    /**
     * 审核状态 - 待审
     */
    int AUDIT_STATUS_PENDING = 0;

    /**
     * 审核状态 - 通过
     */
    int AUDIT_STATUS_PASSED = 1;

    /**
     * 审核状态 - 违规
     */
    int AUDIT_STATUS_REJECTED = 2;
}
