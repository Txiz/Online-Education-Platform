package com.xzx.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 视频点播服务接口
 * 作者: xzx
 * 创建时间: 2021-03-17-14-40
 **/
public interface VodService {

    /**
     * 上传视频至阿里云，并回调视频id
     *
     * @param file 视频
     * @return 视频id
     */
    String upload(MultipartFile file);

    /**
     * 根据视频id，删除视频
     *
     * @param videoId 视频id
     */
    void remove(String videoId);

    /**
     * 根据视频id列表，批量删除视频
     *
     * @param videoIds 视频id列表
     */
    void removeBatch(List<String> videoIds);

    /**
     * 根据视频id，获取视频播放凭证
     *
     * @param videoId 视频id
     * @return 播放凭证
     */
    String getPlayAuth(String videoId);

    /**
     * 根据视频id，获取视频播放地址
     *
     * @param videoId 视频id
     * @return 播放地址
     */
    String getPlayInfo(String videoId);
}
