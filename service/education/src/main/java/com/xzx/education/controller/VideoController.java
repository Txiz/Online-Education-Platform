package com.xzx.education.controller;


import com.xzx.common.result.R;
import com.xzx.education.annotation.OperateLogger;
import com.xzx.education.client.VodClient;
import com.xzx.education.entity.Video;
import com.xzx.education.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
@RestController
@RequestMapping("/education/video")
@Api(tags = "视频控制器")
public class VideoController {

    @Resource
    private VideoService videoService;

    @Resource
    private VodClient vodClient;

    @Transactional
    @OperateLogger(description = "保存或更新视频")
    @ApiOperation(value = "保存或更新视频")
    @PostMapping("/saveOrUpdateVideo")
    public R saveOrUpdateVideo(@RequestBody Video video) {
        videoService.saveOrUpdate(video);
        return R.ok().message("保存或更新视频成功!");
    }

    @Transactional
    @OperateLogger(description = "删除视频")
    @ApiOperation(value = "删除视频")
    @DeleteMapping("/deleteVideo/{videoId}")
    public R deleteVideo(@PathVariable Integer videoId) {
        Video video = videoService.getById(videoId);
        String videoUrl = video.getVideoUrl();
        if (!StringUtils.isEmpty(videoUrl))
            vodClient.remove(videoUrl);
        return R.ok().message("删除视频成功!");
    }
}

