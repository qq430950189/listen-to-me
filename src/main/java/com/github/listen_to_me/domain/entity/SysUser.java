package com.github.listen_to_me.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author ListenToMe Team
 * @since 2026-03-25
 */
@Getter
@Setter
@TableName("sys_user")
@Schema(name = "SysUser", description = "")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "账号")
    @TableField("username")
    private String username;

    @Schema(description = "BCrypt加密")
    @TableField("password")
    private String password;

    @Schema(description = "昵称")
    @TableField("nickname")
    private String nickname;

    @Schema(description = "头像地址")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "手机号")
    @TableField("phone")
    private String phone;

    @Schema(description = "三方平台唯一标识")
    @TableField("openid")
    private String openid;

    @Schema(description = "0-听众, 1-创作者")
    @TableField("is_creator")
    private Boolean isCreator;

    @Schema(description = "可提现余额")
    @TableField("balance")
    private BigDecimal balance;

    @Schema(description = "账期内冻结金额")
    @TableField("frozen_balance")
    private BigDecimal frozenBalance;

    @Schema(description = "乐观锁版本号")
    @TableField("version")
    private Integer version;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
