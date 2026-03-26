package com.github.listen_to_me.controller.creator;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.listen_to_me.common.constant.AudioStatusConstant;
import com.github.listen_to_me.common.exception.BizException;
import com.github.listen_to_me.common.result.Result;
import com.github.listen_to_me.domain.dto.AudioDTO;
import com.github.listen_to_me.domain.entity.AudioInfo;
import com.github.listen_to_me.domain.entity.AudioTagRelation;
import com.github.listen_to_me.domain.query.AudioQuery;
import com.github.listen_to_me.domain.vo.CreatorAudioVO;
import com.github.listen_to_me.service.IAudioInfoService;
import com.github.listen_to_me.service.IAudioTagRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 创作者端音频控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/creator/audio")
@RequiredArgsConstructor
@Tag(name = "创作者端-音频接口", description = "音频发布、管理、AI转写等接口")
public class CreatorAudioController {

    private final IAudioInfoService audioInfoService;
    private final IAudioTagRelationService audioTagRelationService;

    /**
     * 发布音频
     */
    @PostMapping
    @Operation(summary = "发布音频", description = "创作者发布新音频")
    public Result<Void> saveAudio(@RequestBody AudioDTO dto,
                                   @RequestHeader("X-User-Id") Long userId) {
        log.debug("发布音频 - 创作者ID: {}, 标题: {}", userId, dto.getTitle());

        // 创建音频
        AudioInfo audio = new AudioInfo();
        audio.setCreatorId(userId);
        audio.setTitle(dto.getTitle());
        audio.setCoverUrl(dto.getCoverUrl());
        audio.setRawPath(dto.getRawPath());
        audio.setPrice(dto.getPrice() != null ? dto.getPrice() : BigDecimal.ZERO);
        audio.setTrialDuration(dto.getTrialDuration() != null ? dto.getTrialDuration() : 0);
        audio.setAuditStatus(AudioStatusConstant.AUDIT_STATUS_PENDING);
        audio.setStatus(dto.getStatus() != null ? dto.getStatus() : AudioStatusConstant.STATUS_DRAFT);
        audio.setViewCount(0);
        audio.setCreateTime(LocalDateTime.now());

        audioInfoService.save(audio);

        // 保存标签关联
        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            for (Long tagId : dto.getTagIds()) {
                AudioTagRelation relation = new AudioTagRelation();
                relation.setAudioId(audio.getId());
                relation.setTagId(tagId);
                relation.setCreateTime(LocalDateTime.now());
                audioTagRelationService.save(relation);
            }
        }

        log.debug("音频发布成功 - 音频ID: {}", audio.getId());
        return Result.success();
    }

    /**
     * 修改音频
     */
    @PutMapping
    @Operation(summary = "修改音频", description = "修改音频配置信息")
    public Result<Void> updateAudio(@RequestBody AudioDTO dto,
                                     @RequestHeader("X-User-Id") Long userId) {
        log.debug("修改音频 - 音频ID: {}", dto.getId());

        AudioInfo audio = audioInfoService.getById(dto.getId());
        if (audio == null) {
            throw new BizException("音频不存在");
        }

        // 检查权限
        if (!audio.getCreatorId().equals(userId)) {
            throw new BizException("无权修改此音频");
        }

        // 更新字段
        if (dto.getTitle() != null) {
            audio.setTitle(dto.getTitle());
        }
        if (dto.getCoverUrl() != null) {
            audio.setCoverUrl(dto.getCoverUrl());
        }
        if (dto.getPrice() != null) {
            audio.setPrice(dto.getPrice());
        }
        if (dto.getTrialDuration() != null) {
            audio.setTrialDuration(dto.getTrialDuration());
        }
        if (dto.getStatus() != null) {
            audio.setStatus(dto.getStatus());
        }

        audioInfoService.updateById(audio);

        log.debug("音频修改成功 - 音频ID: {}", dto.getId());
        return Result.success();
    }

    /**
     * 删除音频
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除音频", description = "删除音频（逻辑删除）")
    public Result<Void> removeAudio(@PathVariable Long id,
                                     @RequestHeader("X-User-Id") Long userId) {
        log.debug("删除音频 - 音频ID: {}", id);

        AudioInfo audio = audioInfoService.getById(id);
        if (audio == null) {
            throw new BizException("音频不存在");
        }

        // 检查权限
        if (!audio.getCreatorId().equals(userId)) {
            throw new BizException("无权删除此音频");
        }

        audioInfoService.removeById(id);

        log.debug("音频删除成功 - 音频ID: {}", id);
        return Result.success();
    }

    /**
     * 我的音频列表
     */
    @GetMapping("/page")
    @Operation(summary = "我的音频列表", description = "分页查询创作者的音频列表")
    public Result<Page<CreatorAudioVO>> getAudioPage(AudioQuery query,
                                                      @RequestHeader("X-User-Id") Long userId) {
        log.debug("查询我的音频列表 - 创作者ID: {}", userId);

        Page<AudioInfo> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<AudioInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AudioInfo::getCreatorId, userId);

        // 状态筛选
        if (query.getStatus() != null) {
            wrapper.eq(AudioInfo::getStatus, query.getStatus());
        }
        if (query.getAuditStatus() != null) {
            wrapper.eq(AudioInfo::getAuditStatus, query.getAuditStatus());
        }

        // 关键词搜索
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.like(AudioInfo::getTitle, query.getKeyword());
        }

        wrapper.orderByDesc(AudioInfo::getCreateTime);

        Page<AudioInfo> audioPage = audioInfoService.page(page, wrapper);

        // 转换为VO
        Page<CreatorAudioVO> voPage = new Page<>(audioPage.getCurrent(), audioPage.getSize(), audioPage.getTotal());
        voPage.setRecords(audioPage.getRecords().stream().map(audio -> {
            CreatorAudioVO vo = new CreatorAudioVO();
            vo.setId(audio.getId());
            vo.setTitle(audio.getTitle());
            vo.setCoverUrl(audio.getCoverUrl());
            vo.setPrice(audio.getPrice());
            vo.setStatus(audio.getStatus());
            vo.setAuditStatus(audio.getAuditStatus());
            vo.setViewCount(audio.getViewCount());
            vo.setIncome(BigDecimal.ZERO); // 实际项目中应计算收入
            vo.setCreateTime(audio.getCreateTime());
            return vo;
        }).toList());

        return Result.success(voPage);
    }

    /**
     * 音频详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "音频详情", description = "获取音频详细信息（包含转写文本）")
    public Result<CreatorAudioVO> getAudio(@PathVariable Long id,
                                            @RequestHeader("X-User-Id") Long userId) {
        log.debug("查询音频详情 - 音频ID: {}", id);

        AudioInfo audio = audioInfoService.getById(id);
        if (audio == null) {
            throw new BizException("音频不存在");
        }

        // 检查权限
        if (!audio.getCreatorId().equals(userId)) {
            throw new BizException("无权查看此音频");
        }

        CreatorAudioVO vo = new CreatorAudioVO();
        vo.setId(audio.getId());
        vo.setTitle(audio.getTitle());
        vo.setCoverUrl(audio.getCoverUrl());
        vo.setPrice(audio.getPrice());
        vo.setStatus(audio.getStatus());
        vo.setAuditStatus(audio.getAuditStatus());
        vo.setViewCount(audio.getViewCount());
        vo.setCreateTime(audio.getCreateTime());
        vo.setUpdateTime(LocalDateTime.now());

        return Result.success(vo);
    }
}
