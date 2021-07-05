package com.xzx.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import com.xzx.vod.client.VodInitClient;
import com.xzx.vod.service.VodService;
import com.xzx.vod.util.VodPropUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 视频点播服务实现
 * 作者: xzx
 * 创建时间: 2021-03-17-14-40
 **/
@Service
public class VodServiceImpl implements VodService {

    /**
     * 上传视频至阿里云，并回调视频id
     *
     * @param file 视频
     * @return 视频id
     */
    @Override
    public String upload(MultipartFile file) {
        // 获取上传视频的原始名称
        String fileName = file.getOriginalFilename();
        // 获取上传之后显示名称
        assert fileName != null;
        String title = fileName.substring(0, fileName.lastIndexOf("."));
        // inputStream
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 上传视频至阿里云
        UploadStreamRequest request = new UploadStreamRequest(VodPropUtil.KEY_ID, VodPropUtil.KEY_SECRET, title, fileName, inputStream);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        return response.getVideoId();
    }

    /**
     * 根据视频id，删除视频
     *
     * @param videoId 视频id
     */
    @Override
    public void remove(String videoId) {
        DefaultAcsClient defaultAcsClient = VodInitClient.init(VodPropUtil.KEY_ID, VodPropUtil.KEY_SECRET);
        DeleteVideoRequest request = new DeleteVideoRequest();
        request.setVideoIds(videoId);
        try {
            defaultAcsClient.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据视频id列表，批量删除视频
     *
     * @param videoIds 视频id列表
     */
    @Override
    public void removeBatch(List<String> videoIds) {
        DefaultAcsClient defaultAcsClient = VodInitClient.init(VodPropUtil.KEY_ID, VodPropUtil.KEY_SECRET);
        DeleteVideoRequest request = new DeleteVideoRequest();
        String join = StringUtils.join(videoIds.toArray(), ",");
        request.setVideoIds(join);
        try {
            defaultAcsClient.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据视频id，获取视频播放凭证
     *
     * @param videoId 视频id
     * @return 播放凭证
     */
    @Override
    public String getPlayAuth(String videoId) {
        DefaultAcsClient defaultAcsClient = VodInitClient.init(VodPropUtil.KEY_ID, VodPropUtil.KEY_SECRET);
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoId);
        GetVideoPlayAuthResponse response = null;
        try {
            response = defaultAcsClient.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        assert response != null;
        return response.getPlayAuth();
    }

    /**
     * 根据视频id，获取视频播放地址
     *
     * @param videoId 视频id
     * @return 播放地址
     */
    @Override
    public String getPlayInfo(String videoId) {
        DefaultAcsClient defaultAcsClient = VodInitClient.init(VodPropUtil.KEY_ID, VodPropUtil.KEY_SECRET);
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId(videoId);
        GetPlayInfoResponse response = null;
        try {
            response = defaultAcsClient.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        assert response != null;
        return response.getPlayInfoList().get(0).getPlayURL();
    }
}
