package com.github.listen_to_me.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 上传URL响应VO
 */
@Data
@Schema(name = "UploadUrlVO", description = "上传URL信息")
public class UploadUrlVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "上传URL")
    private String uploadUrl;

    @Schema(description = "对象键")
    private String objectKey;
}
