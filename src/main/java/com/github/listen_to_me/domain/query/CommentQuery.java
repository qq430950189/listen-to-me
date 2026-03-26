package com.github.listen_to_me.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 评论查询条件
 */
@Data
@Schema(name = "CommentQuery", description = "评论查询条件")
public class CommentQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "当前页", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = 10;

    @Schema(description = "音频ID")
    private Long audioId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "是否只查询一级评论")
    private Boolean rootOnly = true;
}
