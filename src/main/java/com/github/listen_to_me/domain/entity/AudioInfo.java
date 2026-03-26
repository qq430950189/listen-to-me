package com.github.listen_to_me.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author ListenToMe Team
 * @since 2026-03-25
 */
@Getter
@Setter
@TableName("audio_info")
@Schema(name = "AudioInfo", description = "")
public class AudioInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "对应 sys_user.id")
    @TableField("creator_id")
    private Long creatorId;

    @TableField("title")
    private String title;

    @TableField("cover_url")
    private String coverUrl;

    @Schema(description = "MinIO原始路径")
    @TableField("raw_path")
    private String rawPath;

    @Schema(description = "M3U8路径")
    @TableField("hls_path")
    private String hlsPath;

    @TableField("price")
    private BigDecimal price;

    @Schema(description = "试听秒数")
    @TableField("trial_duration")
    private Integer trialDuration;

    @Schema(description = "0-待审, 1-通过, 2-违规")
    @TableField("audit_status")
    private Integer auditStatus;

    @Schema(description = "0-草稿/下架, 1-转码中, 2-已发布")
    @TableField("status")
    private Integer status;

    @Schema(description = "点击量/热度基数")
    @TableField("view_count")
    private Integer viewCount;

    @TableField("create_time")
    private LocalDateTime createTime;
}
