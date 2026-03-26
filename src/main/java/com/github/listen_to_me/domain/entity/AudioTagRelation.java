package com.github.listen_to_me.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 音频标签关联表
 * </p>
 *
 * @author ListenToMe Team
 * @since 2026-03-25
 */
@Getter
@Setter
@TableName("audio_tag_relation")
@Schema(name = "AudioTagRelation", description = "音频标签关联表")
public class AudioTagRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "代理主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "音频ID")
    @TableField("audio_id")
    private Long audioId;

    @Schema(description = "标签ID")
    @TableField("tag_id")
    private Long tagId;

    @Schema(description = "关联创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;
}
