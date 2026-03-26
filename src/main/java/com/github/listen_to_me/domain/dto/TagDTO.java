package com.github.listen_to_me.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 标签请求DTO
 */
@Data
@Schema(name = "TagDTO", description = "标签请求")
public class TagDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "标签ID（修改时必填）")
    private Long id;

    @Schema(description = "标签名称", example = "后端技术")
    @NotBlank(message = "标签名称不能为空")
    private String name;
}
