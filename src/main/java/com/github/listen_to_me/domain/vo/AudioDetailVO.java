package com.github.listen_to_me.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 音频详情VO
 */
@Data
@Schema(name = "AudioDetailVO", description = "音频详情信息")
public class AudioDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    // ========== 基础信息 ==========
    @Schema(description = "音频ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "封面URL")
    private String coverUrl;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "试听秒数")
    private Integer trialDuration;

    @Schema(description = "HLS播放路径")
    private String hlsPath;

    @Schema(description = "当前用户是否已购买")
    private Boolean isPurchased;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    // ========== 创作者信息 ==========
    @Schema(description = "创作者信息")
    private UserVO creator;

    // ========== 转写信息 ==========
    @Schema(description = "转写信息")
    private TranscriptVO transcript;

    // ========== 统计信息 ==========
    @Schema(description = "统计信息")
    private AudioStatsVO stats;

    // ========== 标签 ==========
    @Schema(description = "标签列表")
    private List<String> tags;

    /**
     * 转写信息VO
     */
    @Data
    @Schema(name = "TranscriptVO", description = "转写信息")
    public static class TranscriptVO implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "任务ID")
        private String taskId;

        @Schema(description = "状态：0-待处理，1-处理中，2-完成，3-失败")
        private Integer status;

        @Schema(description = "完整文本")
        private String fullText;

        @Schema(description = "分段信息")
        private List<SegmentVO> segments;
    }

    /**
     * 分段信息VO
     */
    @Data
    @Schema(name = "SegmentVO", description = "分段信息")
    public static class SegmentVO implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "开始时间（秒）")
        private Integer startTime;

        @Schema(description = "结束时间（秒）")
        private Integer endTime;

        @Schema(description = "标题/文本")
        private String title;
    }

    /**
     * 音频统计VO
     */
    @Data
    @Schema(name = "AudioStatsVO", description = "音频统计")
    public static class AudioStatsVO implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "播放量")
        private Integer viewCount;

        @Schema(description = "收藏数")
        private Integer collectCount;

        @Schema(description = "点赞数")
        private Integer likeCount;

        @Schema(description = "评论数")
        private Integer commentCount;
    }
}
