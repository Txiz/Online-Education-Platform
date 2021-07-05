package com.xzx.education.client;

import com.xzx.common.result.R;
import com.xzx.education.client.impl.VodClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 服务转发，视频服务
 * 作者: xzx
 * 创建时间: 2021-03-19-13-24
 **/
@FeignClient(name = "vod-api", fallback = VodClientImpl.class)
@Component
public interface VodClient {

    @DeleteMapping("/vod/remove/{videoId}")
    R remove(@PathVariable("videoId") String videoId);

    @DeleteMapping("/vod/removeBatch")
    R removeBatch(@RequestParam("videoIds") List<String> videoIds);

    @GetMapping("/vod/getPlayAuth/{videoId}")
    R getPlayAuth(@PathVariable("videoId") String videoId);
}
