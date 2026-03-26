package com.github.listen_to_me.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 创作者音频列表VO
 */
@Data
@Schema(name = "CreatorAudioVO", description = "创作者音频列表信息")
public class CreatorAudioVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "音频ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "封面URL")
    private String coverUrl;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "状态：0-草稿，1-转码中，2-已发布")
    private Integer status;

    @Schema(description = "审核状态：0-待审，1-通过，2-违规")
    private Integer auditStatus;

    @Schema(description = "播放量")
    private Integer viewCount;

    @Schema(description = "收入")
    private BigDecimal income;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "标签列表")
    private List<String> tags;
}
