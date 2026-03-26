package com.github.listen_to_me.controller.creator;

import com.github.listen_to_me.common.exception.BizException;
import com.github.listen_to_me.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 创作者端AI控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/creator/ai")
@RequiredArgsConstructor
@Tag(name = "创作者端-AI接口", description = "AI转写、智能摘要等接口")
public class CreatorAiController {

    /**
     * 申请AI转写
     */
    @PostMapping("/transcript")
    @Operation(summary = "申请AI转写", description = "申请对音频进行AI语音转文字")
    public Result<String> saveAiTranscript(@RequestParam Long audioId,
                                            @RequestHeader("X-User-Id") Long userId) {
        log.debug("申请AI转写 - 音频ID: {}", audioId);

        // 实际项目中应调用AI服务
        String taskId = "task_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);

        return Result.success(taskId);
    }

    /**
     * 申请AI摘要
     */
    @PostMapping("/note")
    @Operation(summary = "申请AI摘要", description = "申请对音频转写文本生成智能摘要")
    public Result<String> saveAiNote(@RequestParam Long audioId,
                                      @RequestHeader("X-User-Id") Long userId) {
        log.debug("申请AI摘要 - 音频ID: {}", audioId);

        // 实际项目中应调用AI服务
        String taskId = "note_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);

        return Result.success(taskId);
    }

    /**
     * 查询AI任务状态
     */
    @GetMapping("/task/{taskId}")
    @Operation(summary = "查询AI任务状态", description = "查询AI转写/摘要任务的处理进度")
    public Result<Map<String, Object>> getAiTask(@PathVariable String taskId) {
        log.debug("查询AI任务状态 - 任务ID: {}", taskId);

        // 实际项目中应查询任务状态
        Map<String, Object> result = new HashMap<>();
        result.put("taskId", taskId);
        result.put("status", 2); // 0-待处理，1-处理中，2-完成，3-失败
        result.put("progress", 100);

        return Result.success(result);
    }
}
