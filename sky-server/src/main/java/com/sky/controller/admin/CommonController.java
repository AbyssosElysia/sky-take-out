package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@Slf4j
@RestController
@RequestMapping("/admin/common")
public class CommonController {

    @Autowired
    private MinioUtil minioUtil;

    @PostMapping("upload")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传:{}", file.getOriginalFilename());

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //构造新文件名称
        String objectName = UUID.randomUUID().toString() + extension;

        try {
            minioUtil.upload(objectName, file.getInputStream(), file.getSize());
            String url = minioUtil.getUrl(objectName);

            return Result.success(url);
        } catch (IOException e) {
            log.error("文件上传失败", e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
