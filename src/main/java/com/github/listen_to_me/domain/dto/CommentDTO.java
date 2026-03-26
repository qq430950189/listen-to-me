package com.github.listen_to_me.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 评论请求DTO
 */
@Data
@Schema(name = "CommentDTO", description = "评论请求")
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "音频ID")
    @NotNull(message = "音频ID不能为空")
    private Long audioId;

    @Schema(description = "父评论ID（回复时必填）")
    private Long parentId;

    @Schema(description = "评论内容")
    @NotBlank(message = "评论内容不能为空")
    private String content;
}
