package com.github.listen_to_me.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求DTO
 */
@Data
@Schema(name = "UserRegisterDTO", description = "用户注册请求")
public class UserRegisterDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名", example = "user001")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", example = "123456")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "昵称", example = "听众小王")
    private String nickname;

    @Schema(description = "手机号", example = "13800138000")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "是否注册为创作者", example = "false")
    private Boolean isCreator;
}
