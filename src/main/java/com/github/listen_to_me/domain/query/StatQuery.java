package com.github.listen_to_me.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 统计查询条件
 */
@Data
@Schema(name = "StatQuery", description = "统计查询条件")
public class StatQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "开始日期", example = "2026-03-01")
    private String startDate;

    @Schema(description = "结束日期", example = "2026-03-25")
    private String endDate;

    @Schema(description = "时间粒度：day, week, month")
    private String granularity = "day";
}
