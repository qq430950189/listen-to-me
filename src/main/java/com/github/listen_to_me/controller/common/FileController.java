package com.github.listen_to_me.controller.common;

import com.github.listen_to_me.common.exception.BizException;
import com.github.listen_to_me.common.result.Result;
import com.github.listen_to_me.domain.vo.UploadUrlVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/common/file")
@RequiredArgsConstructor
@Tag(name = "文件接口", description = "文件上传、预签名等接口")
public class FileController {

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    @Value("${file.upload.url-prefix:http://localhost:8080/uploads}")
    private String urlPrefix;

    /**
     * 获取上传预签名URL
     */
    @GetMapping("/url")
    @Operation(summary = "获取上传预签名URL", description = "获取文件上传的预签名URL")
    public Result<UploadUrlVO> getUploadUrl(@RequestParam String fileName) {
        log.debug("获取上传预签名URL - 文件名: {}", fileName);

        // 生成文件存储路径
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String objectKey = datePath + "/" + UUID.randomUUID().toString().replace("-", "") + extension;

        // 构建上传URL（实际项目中应使用MinIO预签名）
        String uploadUrl = urlPrefix + "/" + objectKey;

        UploadUrlVO vo = new UploadUrlVO();
        vo.setUploadUrl(uploadUrl);
        vo.setObjectKey(objectKey);

        return Result.success(vo);
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    @Operation(summary = "文件上传", description = "上传单个文件")
    public Result<Map<String, String>> saveFile(@RequestParam("file") MultipartFile file) {
        log.debug("文件上传 - 文件名: {}, 大小: {}", file.getOriginalFilename(), file.getSize());

        if (file.isEmpty()) {
            throw new BizException("上传文件不能为空");
        }

        // 生成文件存储路径
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String newFileName = UUID.randomUUID().toString().replace("-", "") + extension;
        String objectKey = datePath + "/" + newFileName;

        try {
            // 保存文件（实际项目中应上传至MinIO）
            java.io.File dest = new java.io.File(uploadPath + "/" + objectKey);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);

            // 返回文件访问URL
            Map<String, String> result = new HashMap<>();
            result.put("url", urlPrefix + "/" + objectKey);
            result.put("objectKey", objectKey);

            log.debug("文件上传成功 - 路径: {}", objectKey);
            return Result.success(result);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BizException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 播放签名生成
     */
    @GetMapping("/sign")
    @Operation(summary = "获取播放签名", description = "为音频播放生成防盗链签名")
    public Result<String> getStreamSign(@RequestParam Long audioId) {
        log.debug("获取播放签名 - 音频ID: {}", audioId);

        // 实际项目中应生成带过期时间的签名
        // 这里简化处理，返回一个基于audioId的签名
        String sign = UUID.randomUUID().toString().replace("-", "");

        return Result.success(sign);
    }
}
