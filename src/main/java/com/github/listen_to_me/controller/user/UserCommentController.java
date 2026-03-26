package com.github.listen_to_me.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.listen_to_me.common.result.Result;
import com.github.listen_to_me.domain.dto.CommentDTO;
import com.github.listen_to_me.domain.entity.PlayHistory;
import com.github.listen_to_me.domain.query.CommentQuery;
import com.github.listen_to_me.domain.query.HistoryQuery;
import com.github.listen_to_me.domain.vo.AudioVO;
import com.github.listen_to_me.domain.vo.CommentVO;
import com.github.listen_to_me.service.IAudioInfoService;
import com.github.listen_to_me.service.IPlayHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户端评论控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/user/comment")
@RequiredArgsConstructor
@Tag(name = "用户端-评论接口", description = "评论、回复等接口")
public class UserCommentController {

    private final IPlayHistoryService playHistoryService;
    private final IAudioInfoService audioInfoService;

    /**
     * 发表评论
     */
    @PostMapping
    @Operation(summary = "发表评论", description = "对音频发表评论或回复")
    public Result<Void> saveComment(@RequestBody CommentDTO dto,
                                     @RequestHeader("X-User-Id") Long userId) {
        log.debug("发表评论 - 用户ID: {}, 音频ID: {}", userId, dto.getAudioId());

        // 实际项目中应保存评论
        // 这里简化处理

        return Result.success();
    }

    /**
     * 评论列表
     */
    @GetMapping("/page")
    @Operation(summary = "评论列表", description = "分页查询音频评论")
    public Result<Page<CommentVO>> getCommentPage(CommentQuery query) {
        log.debug("查询评论列表 - 音频ID: {}", query.getAudioId());

        // 实际项目中应查询评论
        // 这里返回空列表
        Page<CommentVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        page.setRecords(new ArrayList<>());
        page.setTotal(0);

        return Result.success(page);
    }
}
