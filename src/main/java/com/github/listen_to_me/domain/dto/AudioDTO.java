package com.github.listen_to_me.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 音频请求DTO
 */
@Data
@Schema(name = "AudioDTO", description = "音频请求")
public class AudioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "音频ID（修改时必填）")
    private Long id;

    @Schema(description = "标题", example = "Spring Boot 核心原理解析")
    @NotBlank(message = "标题不能为空")
    private String title;

    @Schema(description = "封面URL")
    private String coverUrl;

    @Schema(description = "MinIO原始路径")
    private String rawPath;

    @Schema(description = "价格", example = "19.90")
    private BigDecimal price;

    @Schema(description = "试听秒数", example = "300")
    private Integer trialDuration;

    @Schema(description = "标签ID列表")
    private List<Long> tagIds;

    @Schema(description = "状态：0-草稿，1-发布", example = "1")
    private Integer status;
}
