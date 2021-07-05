package com.xzx.education.client.impl;

import com.xzx.common.result.R;
import com.xzx.education.client.VodClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Vod回调实现
 * 作者: xzx
 * 创建时间: 2021-03-19-13-33
 **/
@Component
public class VodClientImpl implements VodClient {

    @Override
    public R remove(String videoId) {
        return R.error().message("删除视频发生错误");
    }

    @Override
    public R removeBatch(List<String> videoIds) {
        return R.error().message("批量删除视频发生错误");
    }

    @Override
    public R getPlayAuth(String videoId) {
        return R.error().message("获取视频播放凭证发生错误");
    }
}
