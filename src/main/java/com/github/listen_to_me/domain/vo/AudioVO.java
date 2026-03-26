package com.github.listen_to_me.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 音频列表VO
 */
@Data
@Schema(name = "AudioVO", description = "音频列表信息")
public class AudioVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "音频ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "封面URL")
    private String coverUrl;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "时长（秒）")
    private Integer duration;

    @Schema(description = "试听秒数")
    private Integer trialDuration;

    @Schema(description = "创作者名称")
    private String creatorName;

    @Schema(description = "创作者头像")
    private String creatorAvatar;

    @Schema(description = "播放量")
    private Integer viewCount;

    @Schema(description = "标签列表")
    private List<String> tags;

    @Schema(description = "当前用户是否已购买")
    private Boolean isPurchased;
}
