package com.github.listen_to_me.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 财务流水查询条件
 */
@Data
@Schema(name = "FinanceQuery", description = "财务流水查询条件")
public class FinanceQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "当前页", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = 10;

    @Schema(description = "类型：income-收入，withdraw-提现")
    private String type;

    @Schema(description = "开始日期")
    private String startDate;

    @Schema(description = "结束日期")
    private String endDate;
}
