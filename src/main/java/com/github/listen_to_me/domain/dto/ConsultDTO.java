package com.github.listen_to_me.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 咨询预约请求DTO
 */
@Data
@Schema(name = "ConsultDTO", description = "咨询预约请求")
public class ConsultDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "时间槽ID")
    @NotNull(message = "时间槽ID不能为空")
    private Long slotId;

    @Schema(description = "咨询问题")
    private String question;
}
