package com.github.listen_to_me.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.listen_to_me.common.constant.AudioStatusConstant;
import com.github.listen_to_me.common.exception.BizException;
import com.github.listen_to_me.common.result.Result;
import com.github.listen_to_me.domain.dto.AuditDTO;
import com.github.listen_to_me.domain.entity.AudioInfo;
import com.github.listen_to_me.domain.query.AudioQuery;
import com.github.listen_to_me.domain.vo.CreatorAudioVO;
import com.github.listen_to_me.service.IAudioInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员端音频控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/audio")
@RequiredArgsConstructor
@Tag(name = "管理员端-音频接口", description = "音频审核、管理等接口")
public class AdminAudioController {

    private final IAudioInfoService audioInfoService;

    /**
     * 待审音频分页
     */
    @GetMapping("/audit/page")
    @Operation(summary = "待审音频分页", description = "分页查询待审核音频列表")
    public Result<Page<CreatorAudioVO>> getAudioPage(AudioQuery query) {
        log.debug("查询待审音频列表 - 页码: {}", query.getPageNum());

        Page<AudioInfo> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<AudioInfo> wrapper = new LambdaQueryWrapper<>();

        // 查询待审音频
        wrapper.eq(AudioInfo::getAuditStatus, AudioStatusConstant.AUDIT_STATUS_PENDING);
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
            vo.setCreateTime(audio.getCreateTime());
            return vo;
        }).toList());

        return Result.success(voPage);
    }

    /**
     * 执行音频审核
     */
    @PutMapping("/audit")
    @Operation(summary = "执行音频审核", description = "审核通过或拒绝音频")
    public Result<Void> updateAudioAudit(@RequestBody AuditDTO dto) {
        log.debug("执行音频审核 - 音频ID: {}, 状态: {}", dto.getAudioId(), dto.getAuditStatus());

        AudioInfo audio = audioInfoService.getById(dto.getAudioId());
        if (audio == null) {
            throw new BizException("音频不存在");
        }

        audio.setAuditStatus(dto.getAuditStatus());

        // 如果审核通过且状态为草稿，则自动发布
        if (dto.getAuditStatus() == AudioStatusConstant.AUDIT_STATUS_PASSED
                && audio.getStatus() == AudioStatusConstant.STATUS_DRAFT) {
            audio.setStatus(AudioStatusConstant.STATUS_PUBLISHED);
        }

        audioInfoService.updateById(audio);

        log.debug("音频审核成功 - 音频ID: {}", dto.getAudioId());
        return Result.success();
    }

    /**
     * 手动调整热度
     */
    @PutMapping("/hot")
    @Operation(summary = "手动调整热度", description = "人工调整音频热度权重")
    public Result<Void> updateAudioHot(@RequestParam Long audioId,
                                        @RequestParam Integer viewCount) {
        log.debug("调整音频热度 - 音频ID: {}, 热度: {}", audioId, viewCount);

        AudioInfo audio = audioInfoService.getById(audioId);
        if (audio == null) {
            throw new BizException("音频不存在");
        }

        audio.setViewCount(viewCount);
        audioInfoService.updateById(audio);

        log.debug("热度调整成功 - 音频ID: {}", audioId);
        return Result.success();
    }

    /**
     * 下架音频
     */
    @PutMapping("/{id}/offline")
    @Operation(summary = "下架音频", description = "强制下架音频")
    public Result<Void> updateAudioOffline(@PathVariable Long id) {
        log.debug("下架音频 - 音频ID: {}", id);

        AudioInfo audio = audioInfoService.getById(id);
        if (audio == null) {
            throw new BizException("音频不存在");
        }

        audio.setStatus(AudioStatusConstant.STATUS_DRAFT);
        audioInfoService.updateById(audio);

        log.debug("音频下架成功 - 音频ID: {}", id);
        return Result.success();
    }
}
