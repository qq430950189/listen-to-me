package com.github.listen_to_me.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 音频查询条件
 */
@Data
@Schema(name = "AudioQuery", description = "音频查询条件")
public class AudioQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "当前页", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = 10;

    @Schema(description = "关键词（标题）")
    private String keyword;

    @Schema(description = "标签ID")
    private Long tagId;

    @Schema(description = "创作者ID")
    private Long creatorId;

    @Schema(description = "状态：0-草稿，1-转码中，2-已发布")
    private Integer status;

    @Schema(description = "审核状态：0-待审，1-通过，2-违规")
    private Integer auditStatus;

    @Schema(description = "排序字段：createTime, viewCount, price")
    private String orderBy;

    @Schema(description = "排序方向：asc, desc")
    private String orderDir = "desc";

    @Schema(description = "最低价格")
    private java.math.BigDecimal minPrice;

    @Schema(description = "最高价格")
    private java.math.BigDecimal maxPrice;
}
