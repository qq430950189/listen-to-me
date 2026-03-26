package com.github.listen_to_me.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 咨询订单VO
 */
@Data
@Schema(name = "ConsultOrderVO", description = "咨询订单信息")
public class ConsultOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "时间槽ID")
    private Long slotId;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "状态：0-可选，1-锁定，2-已约，3-完成")
    private Integer status;

    @Schema(description = "创作者昵称")
    private String creatorNickname;

    @Schema(description = "咨询问题")
    private String question;
}
