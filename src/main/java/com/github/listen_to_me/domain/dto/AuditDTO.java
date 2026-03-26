package com.github.listen_to_me.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 审核请求DTO
 */
@Data
@Schema(name = "AuditDTO", description = "审核请求")
public class AuditDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "音频ID")
    @NotNull(message = "音频ID不能为空")
    private Long audioId;

    @Schema(description = "审核状态：1-通过，2-违规")
    @NotNull(message = "审核状态不能为空")
    private Integer auditStatus;

    @Schema(description = "审核备注")
    private String remark;
}
