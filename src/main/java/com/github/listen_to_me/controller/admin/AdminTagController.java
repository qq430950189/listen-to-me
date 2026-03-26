package com.github.listen_to_me.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.listen_to_me.common.exception.BizException;
import com.github.listen_to_me.common.result.Result;
import com.github.listen_to_me.domain.dto.TagDTO;
import com.github.listen_to_me.domain.entity.SysTag;
import com.github.listen_to_me.service.ISysTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员端标签控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/tag")
@RequiredArgsConstructor
@Tag(name = "管理员端-标签接口", description = "标签管理接口")
public class AdminTagController {

    private final ISysTagService tagService;

    /**
     * 新增标签
     */
    @PostMapping
    @Operation(summary = "新增标签", description = "创建新的标签")
    public Result<Void> saveTag(@RequestBody TagDTO dto) {
        log.debug("新增标签 - 名称: {}", dto.getName());

        // 检查是否已存在
        long count = tagService.count(new LambdaQueryWrapper<SysTag>()
                .eq(SysTag::getName, dto.getName()));
        if (count > 0) {
            throw new BizException("标签名称已存在");
        }

        SysTag tag = new SysTag();
        tag.setName(dto.getName());

        tagService.save(tag);

        log.debug("标签创建成功 - ID: {}", tag.getId());
        return Result.success();
    }

    /**
     * 修改标签
     */
    @PutMapping
    @Operation(summary = "修改标签", description = "修改标签信息")
    public Result<Void> updateTag(@RequestBody TagDTO dto) {
        log.debug("修改标签 - ID: {}", dto.getId());

        SysTag tag = tagService.getById(dto.getId());
        if (tag == null) {
            throw new BizException("标签不存在");
        }

        // 检查名称是否重复
        long count = tagService.count(new LambdaQueryWrapper<SysTag>()
                .eq(SysTag::getName, dto.getName())
                .ne(SysTag::getId, dto.getId()));
        if (count > 0) {
            throw new BizException("标签名称已存在");
        }

        tag.setName(dto.getName());
        tagService.updateById(tag);

        log.debug("标签修改成功 - ID: {}", dto.getId());
        return Result.success();
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除标签", description = "删除标签")
    public Result<Void> removeTag(@PathVariable Long id) {
        log.debug("删除标签 - ID: {}", id);

        tagService.removeById(id);

        log.debug("标签删除成功 - ID: {}", id);
        return Result.success();
    }

    /**
     * 标签列表
     */
    @GetMapping("/list")
    @Operation(summary = "标签列表", description = "获取所有标签列表")
    public Result<List<SysTag>> getTagList() {
        log.debug("查询标签列表");

        List<SysTag> tags = tagService.list();
        return Result.success(tags);
    }

    /**
     * 标签分页
     */
    @GetMapping("/page")
    @Operation(summary = "标签分页", description = "分页查询标签")
    public Result<Page<SysTag>> getTagPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @RequestParam(required = false) String name) {
        log.debug("查询标签分页 - 页码: {}", pageNum);

        Page<SysTag> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysTag> wrapper = new LambdaQueryWrapper<>();

        if (name != null && !name.isEmpty()) {
            wrapper.like(SysTag::getName, name);
        }

        wrapper.orderByDesc(SysTag::getId);

        tagService.page(page, wrapper);
        return Result.success(page);
    }
}
