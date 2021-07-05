package com.xzx.vod.client;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;

/**
 * 视频点播初始化工具
 * 作者: xzx
 * 创建时间: 2021-03-17-14-36
 **/
public class VodInitClient {
    public static DefaultAcsClient init(String accessKeyId, String accessKeySecret) {
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        return new DefaultAcsClient(profile);
    }
}
