package com.github.listen_to_me.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.listen_to_me.common.constant.AudioStatusConstant;
import com.github.listen_to_me.common.exception.BizException;
import com.github.listen_to_me.common.result.Result;
import com.github.listen_to_me.domain.entity.AudioInfo;
import com.github.listen_to_me.domain.entity.AudioTagRelation;
import com.github.listen_to_me.domain.entity.OrderInfo;
import com.github.listen_to_me.domain.entity.SysTag;
import com.github.listen_to_me.domain.entity.SysUser;
import com.github.listen_to_me.domain.query.AudioQuery;
import com.github.listen_to_me.domain.vo.AudioDetailVO;
import com.github.listen_to_me.domain.vo.AudioVO;
import com.github.listen_to_me.domain.vo.UserVO;
import com.github.listen_to_me.service.IAudioInfoService;
import com.github.listen_to_me.service.IAudioTagRelationService;
import com.github.listen_to_me.service.IOrderInfoService;
import com.github.listen_to_me.service.ISysTagService;
import com.github.listen_to_me.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户端音频控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/user/audio")
@RequiredArgsConstructor
@Tag(name = "用户端-音频接口", description = "音频发现、播放、收藏等接口")
public class UserAudioController {

    private final IAudioInfoService audioInfoService;
    private final ISysUserService userService;
    private final ISysTagService tagService;
    private final IAudioTagRelationService audioTagRelationService;
    private final IOrderInfoService orderInfoService;

    /**
     * 音频分页列表
     */
    @GetMapping("/page")
    @Operation(summary = "音频分页列表", description = "分页查询音频列表")
    public Result<Page<AudioVO>> getAudioPage(AudioQuery query,
                                               @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        log.debug("查询音频分页列表 - 页码: {}, 大小: {}", query.getPageNum(), query.getPageSize());

        // 构建查询条件
        Page<AudioInfo> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<AudioInfo> wrapper = new LambdaQueryWrapper<>();

        // 只查询已发布的音频
        wrapper.eq(AudioInfo::getStatus, AudioStatusConstant.STATUS_PUBLISHED);
        wrapper.eq(AudioInfo::getAuditStatus, AudioStatusConstant.AUDIT_STATUS_PASSED);

        // 关键词搜索
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.like(AudioInfo::getTitle, query.getKeyword());
        }

        // 排序
        wrapper.orderByDesc(AudioInfo::getCreateTime);

        Page<AudioInfo> audioPage = audioInfoService.page(page, wrapper);

        // 转换为VO
        Page<AudioVO> voPage = convertToAudioVOPage(audioPage, userId);

        return Result.success(voPage);
    }

    /**
     * 热门音频列表
     */
    @GetMapping("/hot")
    @Operation(summary = "热门音频列表", description = "获取热门音频排行")
    public Result<List<AudioVO>> getAudioList(@RequestParam(defaultValue = "10") Integer limit,
                                               @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        log.debug("查询热门音频列表 - 数量: {}", limit);

        LambdaQueryWrapper<AudioInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AudioInfo::getStatus, AudioStatusConstant.STATUS_PUBLISHED);
        wrapper.eq(AudioInfo::getAuditStatus, AudioStatusConstant.AUDIT_STATUS_PASSED);
        wrapper.orderByDesc(AudioInfo::getViewCount);
        wrapper.last("LIMIT " + limit);

        List<AudioInfo> audioList = audioInfoService.list(wrapper);

        List<AudioVO> voList = convertToAudioVOList(audioList, userId);

        return Result.success(voList);
    }

    /**
     * 音频详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "音频详情", description = "获取音频详细信息")
    public Result<AudioDetailVO> getAudio(@PathVariable Long id,
                                           @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        log.debug("查询音频详情 - ID: {}", id);

        AudioInfo audio = audioInfoService.getById(id);
        if (audio == null) {
            throw new BizException("音频不存在");
        }

        AudioDetailVO vo = new AudioDetailVO();
        vo.setId(audio.getId());
        vo.setTitle(audio.getTitle());
        vo.setCoverUrl(audio.getCoverUrl());
        vo.setPrice(audio.getPrice());
        vo.setTrialDuration(audio.getTrialDuration());
        vo.setHlsPath(audio.getHlsPath());
        vo.setCreateTime(audio.getCreateTime());

        // 查询创作者信息
        SysUser creator = userService.getById(audio.getCreatorId());
        if (creator != null) {
            UserVO creatorVO = new UserVO();
            creatorVO.setId(creator.getId());
            creatorVO.setUsername(creator.getUsername());
            creatorVO.setNickname(creator.getNickname());
            creatorVO.setAvatar(creator.getAvatar());
            creatorVO.setIsCreator(creator.getIsCreator());
            vo.setCreator(creatorVO);
        }

        // 检查用户是否已购买
        if (userId != null) {
            long count = orderInfoService.count(new LambdaQueryWrapper<OrderInfo>()
                    .eq(OrderInfo::getUserId, userId)
                    .eq(OrderInfo::getAudioId, id)
                    .eq(OrderInfo::getPayStatus, 1));
            vo.setIsPurchased(count > 0);
        } else {
            vo.setIsPurchased(false);
        }

        // 查询标签
        List<AudioTagRelation> relations = audioTagRelationService.list(
                new LambdaQueryWrapper<AudioTagRelation>().eq(AudioTagRelation::getAudioId, id));
        if (!relations.isEmpty()) {
            List<Long> tagIds = relations.stream().map(AudioTagRelation::getTagId).collect(Collectors.toList());
            List<SysTag> tags = tagService.listByIds(tagIds);
            vo.setTags(tags.stream().map(SysTag::getName).collect(Collectors.toList()));
        }

        // 统计信息
        AudioDetailVO.AudioStatsVO stats = new AudioDetailVO.AudioStatsVO();
        stats.setViewCount(audio.getViewCount());
        stats.setCollectCount(0);
        stats.setLikeCount(0);
        stats.setCommentCount(0);
        vo.setStats(stats);

        return Result.success(vo);
    }

    /**
     * 我的已购音频
     */
    @GetMapping("/my/purchased")
    @Operation(summary = "我的已购音频", description = "获取当前用户已购买的音频列表")
    public Result<Page<AudioVO>> getMyPurchasedPage(AudioQuery query,
                                                     @RequestHeader("X-User-Id") Long userId) {
        log.debug("查询我的已购音频 - 用户ID: {}", userId);

        // 查询已支付订单
        Page<OrderInfo> orderPage = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getUserId, userId);
        wrapper.eq(OrderInfo::getPayStatus, 1);
        wrapper.orderByDesc(OrderInfo::getPayTime);

        orderInfoService.page(orderPage, wrapper);

        // 转换为音频VO
        Page<AudioVO> voPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        if (!orderPage.getRecords().isEmpty()) {
            List<Long> audioIds = orderPage.getRecords().stream()
                    .map(OrderInfo::getAudioId).collect(Collectors.toList());
            List<AudioInfo> audioList = audioInfoService.listByIds(audioIds);
            List<AudioVO> voList = convertToAudioVOList(audioList, userId);
            voPage.setRecords(voList);
        }

        return Result.success(voPage);
    }

    /**
     * 转换音频分页为VO分页
     */
    private Page<AudioVO> convertToAudioVOPage(Page<AudioInfo> audioPage, Long userId) {
        Page<AudioVO> voPage = new Page<>(audioPage.getCurrent(), audioPage.getSize(), audioPage.getTotal());
        voPage.setRecords(convertToAudioVOList(audioPage.getRecords(), userId));
        return voPage;
    }

    /**
     * 转换音频列表为VO列表
     */
    private List<AudioVO> convertToAudioVOList(List<AudioInfo> audioList, Long userId) {
        if (audioList == null || audioList.isEmpty()) {
            return new ArrayList<>();
        }

        List<AudioVO> voList = new ArrayList<>();
        for (AudioInfo audio : audioList) {
            AudioVO vo = new AudioVO();
            vo.setId(audio.getId());
            vo.setTitle(audio.getTitle());
            vo.setCoverUrl(audio.getCoverUrl());
            vo.setPrice(audio.getPrice());
            vo.setTrialDuration(audio.getTrialDuration());
            vo.setViewCount(audio.getViewCount());

            // 查询创作者
            SysUser creator = userService.getById(audio.getCreatorId());
            if (creator != null) {
                vo.setCreatorName(creator.getNickname());
                vo.setCreatorAvatar(creator.getAvatar());
            }

            // 检查是否已购买
            if (userId != null) {
                long count = orderInfoService.count(new LambdaQueryWrapper<OrderInfo>()
                        .eq(OrderInfo::getUserId, userId)
                        .eq(OrderInfo::getAudioId, audio.getId())
                        .eq(OrderInfo::getPayStatus, 1));
                vo.setIsPurchased(count > 0);
            } else {
                vo.setIsPurchased(false);
            }

            voList.add(vo);
        }

        return voList;
    }
}
