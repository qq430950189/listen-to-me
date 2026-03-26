package com.github.listen_to_me.controller.creator;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.listen_to_me.common.constant.SlotStatusConstant;
import com.github.listen_to_me.common.exception.BizException;
import com.github.listen_to_me.common.result.Result;
import com.github.listen_to_me.domain.dto.SlotDTO;
import com.github.listen_to_me.domain.entity.ConsultSlot;
import com.github.listen_to_me.domain.query.ConsultQuery;
import com.github.listen_to_me.domain.vo.ConsultOrderVO;
import com.github.listen_to_me.service.IConsultSlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 创作者端咨询控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/creator/consult")
@RequiredArgsConstructor
@Tag(name = "创作者端-咨询接口", description = "时间槽管理、预约查看等接口")
public class CreatorConsultController {

    private final IConsultSlotService consultSlotService;

    /**
     * 批量生成时间槽
     */
    @PostMapping("/slots")
    @Operation(summary = "批量生成时间槽", description = "创作者批量生成可预约时间槽")
    public Result<Void> saveSlotBatch(@RequestBody List<SlotDTO> dtoList,
                                       @RequestHeader("X-User-Id") Long userId) {
        log.debug("批量生成时间槽 - 创作者ID: {}, 数量: {}", userId, dtoList.size());

        List<ConsultSlot> slots = new ArrayList<>();
        for (SlotDTO dto : dtoList) {
            ConsultSlot slot = new ConsultSlot();
            slot.setCreatorId(userId);
            slot.setStartTime(dto.getStartTime());
            slot.setEndTime(dto.getEndTime());
            slot.setStatus(SlotStatusConstant.STATUS_AVAILABLE);
            slots.add(slot);
        }

        consultSlotService.saveBatch(slots);

        log.debug("时间槽生成成功 - 数量: {}", slots.size());
        return Result.success();
    }

    /**
     * 修改时间槽状态
     */
    @PutMapping("/slots/{id}")
    @Operation(summary = "修改时间槽状态", description = "手动开启或关闭时间槽")
    public Result<Void> updateSlot(@PathVariable Long id,
                                    @RequestBody SlotDTO dto,
                                    @RequestHeader("X-User-Id") Long userId) {
        log.debug("修改时间槽 - ID: {}", id);

        ConsultSlot slot = consultSlotService.getById(id);
        if (slot == null) {
            throw new BizException("时间槽不存在");
        }

        // 检查权限
        if (!slot.getCreatorId().equals(userId)) {
            throw new BizException("无权修改此时间槽");
        }

        if (dto.getStatus() != null) {
            slot.setStatus(dto.getStatus());
        }

        consultSlotService.updateById(slot);

        log.debug("时间槽修改成功 - ID: {}", id);
        return Result.success();
    }

    /**
     * 查看预约订单
     */
    @GetMapping("/page")
    @Operation(summary = "查看预约订单", description = "查看哪些用户预约了自己的时间")
    public Result<Page<ConsultOrderVO>> getConsultPage(ConsultQuery query,
                                                        @RequestHeader("X-User-Id") Long userId) {
        log.debug("查看预约订单 - 创作者ID: {}", userId);

        Page<ConsultSlot> slotPage = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<ConsultSlot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ConsultSlot::getCreatorId, userId);

        // 状态筛选
        if (query.getStatus() != null) {
            wrapper.eq(ConsultSlot::getStatus, query.getStatus());
        }

        wrapper.orderByDesc(ConsultSlot::getStartTime);

        consultSlotService.page(slotPage, wrapper);

        // 转换为VO
        Page<ConsultOrderVO> voPage = new Page<>(slotPage.getCurrent(), slotPage.getSize(), slotPage.getTotal());
        voPage.setRecords(new ArrayList<>());

        return Result.success(voPage);
    }
}
