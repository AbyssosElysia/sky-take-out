package com.sky.controller.admin;

import com.sky.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
public class CommonController {

    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    public Result<String> upload(MultipartFile file) {
        log.info("文件上传:{}", file.getOriginalFilename());
        return null;
    }
}
