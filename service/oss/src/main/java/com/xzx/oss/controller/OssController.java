package com.xzx.oss.controller;

import com.xzx.common.result.R;
import com.xzx.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Oss控制器
 * 作者: xzx
 * 创建时间: 2021-03-17-13-46
 **/
@RestController
@RequestMapping("/oss")
@Api(tags = "图片上传控制器")
public class OssController {
    @Resource
    private OssService ossService;

    @ApiOperation(value = "上传图片")
    @PostMapping("/uploadFile")
    public R uploadFile(MultipartFile file) {
        return R.ok().data("url", ossService.uploadFile(file)).message("图片上传成功");
    }
}
