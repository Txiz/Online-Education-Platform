package com.xzx.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.xzx.oss.service.OssService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.xzx.oss.util.OssPropUtil.*;

/**
 * Oss服务接口实现
 * 作者: xzx
 * 创建时间: 2021-03-17-13-47
 **/
@Service
public class OssServiceImpl implements OssService {

    /**
     * 上传图片
     *
     * @param file 需要上传的图片
     * @return 图片的url
     */
    @Override
    public String uploadFile(MultipartFile file) {
        // 获取上传图片名称
        String originalFilename = file.getOriginalFilename();
        // 使用UUID添加随机值，防止同名文件覆盖
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // 把文件按日期分类
        String date = new DateTime().toString("yyyy/MM/dd");
        // 设置上传后新的文件名
        String fileName = date + "/" + uuid + "-" + originalFilename;
        // 获取需要的常量
        String endpoint = END_POINT;
        String accessKeyId = KEY_ID;
        String accessKeySecret = KEY_SECRET;
        String bucketName = BUCKET_NAME;
        // 创建Oss实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 构造输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 上传文件至阿里云
        ossClient.putObject(bucketName, fileName, inputStream);
        // 关闭实例
        ossClient.shutdown();
        // 获取真实的图片路径
        return "https://" + bucketName + "." + endpoint + "/" + fileName;
    }
}
