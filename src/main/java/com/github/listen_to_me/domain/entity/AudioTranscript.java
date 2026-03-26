package com.github.listen_to_me.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("audio_transcript")
@Schema(name = "AudioTranscript", description = "")
public class AudioTranscript implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("audio_id")
    private Long audioId;

    @Schema(description = "AI转写文本")
    @TableField("full_text")
    private String fullText;

    @Schema(description = "AI分段标题与时间戳 [{time: 0, title: \"...\"}]")
    @TableField("segment_json")
    private String segmentJson;
}
