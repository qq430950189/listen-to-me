package com.github.listen_to_me.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 咨询预约查询条件
 */
@Data
@Schema(name = "ConsultQuery", description = "咨询预约查询条件")
public class ConsultQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "当前页", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = 10;

    @Schema(description = "创作者ID")
    private Long creatorId;

    @Schema(description = "时间槽状态：0-可选，1-锁定，2-已约，3-完成")
    private Integer status;

    @Schema(description = "开始时间（筛选范围）")
    private String startTimeBegin;

    @Schema(description = "结束时间（筛选范围）")
    private String startTimeEnd;
}
