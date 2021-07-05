package com.xzx.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Oss服务接口
 * 作者: xzx
 * 创建时间: 2021-03-17-13-47
 **/
public interface OssService {

    /**
     * 上传图片
     *
     * @param file 需要上传的图片
     * @return 图片的url
     */
    String uploadFile(MultipartFile file);
}
