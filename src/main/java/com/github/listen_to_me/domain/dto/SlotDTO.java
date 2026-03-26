package com.github.listen_to_me.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 咨询时间槽请求DTO
 */
@Data
@Schema(name = "SlotDTO", description = "咨询时间槽请求")
public class SlotDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "时间槽ID（修改时必填）")
    private Long id;

    @Schema(description = "开始时间")
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    @Schema(description = "状态：0-可选，1-锁定，2-已约，3-完成")
    private Integer status;
}
