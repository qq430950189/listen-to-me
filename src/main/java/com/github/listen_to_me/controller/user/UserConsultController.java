package com.github.listen_to_me.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.listen_to_me.common.constant.SlotStatusConstant;
import com.github.listen_to_me.common.exception.BizException;
import com.github.listen_to_me.common.result.Result;
import com.github.listen_to_me.domain.dto.ConsultDTO;
import com.github.listen_to_me.domain.entity.ConsultSlot;
import com.github.listen_to_me.domain.query.ConsultQuery;
import com.github.listen_to_me.domain.vo.ConsultOrderVO;
import com.github.listen_to_me.service.IConsultSlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * 用户端咨询控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/user/consult")
@RequiredArgsConstructor
@Tag(name = "用户端-咨询接口", description = "咨询预约等接口")
public class UserConsultController {

    private final IConsultSlotService consultSlotService;

    /**
     * 预约咨询
     */
    @PostMapping
    @Operation(summary = "预约咨询", description = "预约创作者的咨询时间槽")
    public Result<ConsultOrderVO> saveConsult(@RequestBody ConsultDTO dto,
                                               @RequestHeader("X-User-Id") Long userId) {
        log.debug("预约咨询 - 用户ID: {}, 时间槽ID: {}", userId, dto.getSlotId());

        // 1. 查询时间槽
        ConsultSlot slot = consultSlotService.getById(dto.getSlotId());
        if (slot == null) {
            throw new BizException("时间槽不存在");
        }

        // 2. 检查状态
        if (slot.getStatus() != SlotStatusConstant.STATUS_AVAILABLE) {
            throw new BizException("该时间段已被预约或不可用");
        }

        // 3. 更新时间槽状态
        slot.setStatus(SlotStatusConstant.STATUS_BOOKED);
        consultSlotService.updateById(slot);

        // 4. 返回预约信息
        ConsultOrderVO vo = new ConsultOrderVO();
        vo.setSlotId(dto.getSlotId());
        vo.setStartTime(slot.getStartTime());
        vo.setEndTime(slot.getEndTime());
        vo.setStatus(slot.getStatus());

        log.debug("预约成功 - 时间槽ID: {}", dto.getSlotId());
        return Result.success(vo);
    }

    /**
     * 查询可预约时间槽
     */
    @GetMapping("/slots")
    @Operation(summary = "查询可预约时间槽", description = "查询创作者的可预约时间槽")
    public Result<Page<ConsultOrderVO>> getAvailableSlots(ConsultQuery query) {
        log.debug("查询可预约时间槽 - 创作者ID: {}", query.getCreatorId());

        Page<ConsultSlot> slotPage = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<ConsultSlot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ConsultSlot::getCreatorId, query.getCreatorId());
        wrapper.eq(ConsultSlot::getStatus, SlotStatusConstant.STATUS_AVAILABLE);
        wrapper.orderByAsc(ConsultSlot::getStartTime);

        consultSlotService.page(slotPage, wrapper);

        // 转换为VO
        Page<ConsultOrderVO> voPage = new Page<>(slotPage.getCurrent(), slotPage.getSize(), slotPage.getTotal());
        voPage.setRecords(new ArrayList<>());

        return Result.success(voPage);
    }
}
