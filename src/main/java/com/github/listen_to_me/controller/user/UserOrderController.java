package com.github.listen_to_me.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.listen_to_me.common.constant.OrderStatusConstant;
import com.github.listen_to_me.common.exception.BizException;
import com.github.listen_to_me.common.result.Result;
import com.github.listen_to_me.domain.dto.OrderDTO;
import com.github.listen_to_me.domain.entity.AudioInfo;
import com.github.listen_to_me.domain.entity.OrderInfo;
import com.github.listen_to_me.domain.vo.OrderDetailVO;
import com.github.listen_to_me.domain.vo.OrderPayVO;
import com.github.listen_to_me.service.IAudioInfoService;
import com.github.listen_to_me.service.IOrderInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 用户端订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/user/order")
@RequiredArgsConstructor
@Tag(name = "用户端-订单接口", description = "下单、支付、订单查询等接口")
public class UserOrderController {

    private final IOrderInfoService orderInfoService;
    private final IAudioInfoService audioInfoService;

    /**
     * 创建订单
     */
    @PostMapping
    @Operation(summary = "创建订单", description = "创建音频购买订单")
    public Result<OrderPayVO> saveOrder(@RequestBody OrderDTO dto,
                                         @RequestHeader("X-User-Id") Long userId) {
        log.debug("创建订单 - 用户ID: {}, 音频ID: {}", userId, dto.getAudioId());

        // 1. 查询音频信息
        AudioInfo audio = audioInfoService.getById(dto.getAudioId());
        if (audio == null) {
            throw new BizException("音频不存在");
        }

        // 2. 检查是否已购买
        long count = orderInfoService.count(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getUserId, userId)
                .eq(OrderInfo::getAudioId, dto.getAudioId())
                .eq(OrderInfo::getPayStatus, OrderStatusConstant.PAY_STATUS_PAID));
        if (count > 0) {
            throw new BizException("您已购买过该音频");
        }

        // 3. 检查是否有未支付订单
        OrderInfo existOrder = orderInfoService.getOne(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getUserId, userId)
                .eq(OrderInfo::getAudioId, dto.getAudioId())
                .eq(OrderInfo::getPayStatus, OrderStatusConstant.PAY_STATUS_PENDING));
        if (existOrder != null) {
            // 返回已存在的订单
            OrderPayVO vo = buildOrderPayVO(existOrder);
            return Result.success(vo);
        }

        // 4. 创建订单
        OrderInfo order = new OrderInfo();
        order.setOrderSn(generateOrderSn());
        order.setUserId(userId);
        order.setAudioId(dto.getAudioId());
        order.setPayAmount(audio.getPrice());
        order.setPayStatus(OrderStatusConstant.PAY_STATUS_PENDING);
        order.setPayChannel(dto.getPayChannel());
        order.setCreateTime(LocalDateTime.now());

        orderInfoService.save(order);

        log.debug("订单创建成功 - 订单号: {}", order.getOrderSn());

        // 5. 返回支付信息
        OrderPayVO vo = buildOrderPayVO(order);
        return Result.success(vo);
    }

    /**
     * 查询订单详情
     */
    @GetMapping("/{orderSn}")
    @Operation(summary = "查询订单详情", description = "根据订单号查询订单详情")
    public Result<OrderDetailVO> getOrder(@PathVariable String orderSn,
                                           @RequestHeader("X-User-Id") Long userId) {
        log.debug("查询订单详情 - 订单号: {}", orderSn);

        OrderInfo order = orderInfoService.getOne(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getOrderSn, orderSn)
                .eq(OrderInfo::getUserId, userId));

        if (order == null) {
            throw new BizException("订单不存在");
        }

        OrderDetailVO vo = new OrderDetailVO();
        vo.setId(order.getId());
        vo.setOrderSn(order.getOrderSn());
        vo.setUserId(order.getUserId());
        vo.setAudioId(order.getAudioId());
        vo.setPayAmount(order.getPayAmount());
        vo.setPayStatus(order.getPayStatus());
        vo.setPayChannel(order.getPayChannel());
        vo.setPayTime(order.getPayTime());
        vo.setCreateTime(order.getCreateTime());

        // 查询音频信息
        AudioInfo audio = audioInfoService.getById(order.getAudioId());
        if (audio != null) {
            vo.setAudioTitle(audio.getTitle());
            vo.setAudioCover(audio.getCoverUrl());
        }

        return Result.success(vo);
    }

    /**
     * 取消订单
     */
    @PutMapping("/{orderSn}/cancel")
    @Operation(summary = "取消订单", description = "取消未支付的订单")
    public Result<Void> updateOrderCancel(@PathVariable String orderSn,
                                           @RequestHeader("X-User-Id") Long userId) {
        log.debug("取消订单 - 订单号: {}", orderSn);

        OrderInfo order = orderInfoService.getOne(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getOrderSn, orderSn)
                .eq(OrderInfo::getUserId, userId));

        if (order == null) {
            throw new BizException("订单不存在");
        }

        if (order.getPayStatus() != OrderStatusConstant.PAY_STATUS_PENDING) {
            throw new BizException("只能取消待支付的订单");
        }

        order.setPayStatus(OrderStatusConstant.PAY_STATUS_CANCELLED);
        orderInfoService.updateById(order);

        log.debug("订单取消成功 - 订单号: {}", orderSn);
        return Result.success();
    }

    /**
     * 生成订单号
     */
    private String generateOrderSn() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomStr = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        return "ORD" + dateStr + randomStr;
    }

    /**
     * 构建订单支付VO
     */
    private OrderPayVO buildOrderPayVO(OrderInfo order) {
        OrderPayVO vo = new OrderPayVO();
        vo.setOrderSn(order.getOrderSn());
        vo.setPayAmount(order.getPayAmount());
        vo.setPayStatus(order.getPayStatus());

        // 实际项目中应调用支付接口获取支付URL
        vo.setPayUrl("https://pay.example.com/pay?orderSn=" + order.getOrderSn());

        // 设置订单过期时间（30分钟后）
        vo.setExpireTime(order.getCreateTime().plusMinutes(30));

        return vo;
    }
}
