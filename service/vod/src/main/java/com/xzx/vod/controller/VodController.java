package com.xzx.vod.controller;

import com.xzx.common.result.R;
import com.xzx.vod.service.VodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 视频点播控制器
 * 作者: xzx
 * 创建时间: 2021-03-17-14-38
 **/
@RestController
@RequestMapping("/vod")
@Api(tags = "视频点播控制器")
public class VodController {
    @Resource
    private VodService vodService;

    /**
     * Tips
     * 事实上，这个地方的videoId，对应的是数据库中的videoUrl
     */

    @ApiOperation(value = "上传视频至阿里云")
    @PostMapping("/upload")
    public R upload(MultipartFile file) {
        if (file.isEmpty()) {
            return R.error().message("上传视频不能为空");
        }
        String videoId = vodService.upload(file);
        return R.ok().data("videoUrl", videoId).message("上传视频至阿里云成功!");
    }

    @ApiOperation(value = "删除视频")
    @DeleteMapping("/remove/{videoId}")
    public R remove(@PathVariable String videoId) {
        vodService.remove(videoId);
        return R.ok();
    }

    @ApiOperation(value = "批量删除视频")
    @DeleteMapping("/removeBatch")
    public R removeBatch(@RequestParam("videoIds") List<String> videoIds) {
        vodService.removeBatch(videoIds);
        return R.ok();
    }

    @ApiOperation(value = "根据视频id获取视频播放凭证")
    @GetMapping("/getPlayAuth/{videoId}")
    public R getPlayAuth(@PathVariable String videoId) {
        if (StringUtils.isEmpty(videoId) || videoId.equals("null"))
            return R.error().message("视频url为空");
        String playAuth = vodService.getPlayAuth(videoId);
        return R.ok().data("playAuth", playAuth).message("视频播放凭证已获取!");
    }

    @ApiOperation(value = "根据视频id获取视频播放地址")
    @GetMapping("/getPlayInfo/{videoId}")
    public R getPlayInfo(@PathVariable String videoId) {
        if (StringUtils.isEmpty(videoId) || videoId.equals("null"))
            return R.error().message("视频url为空");
        String playInfo = vodService.getPlayInfo(videoId);
        return R.ok().data("playInfo", playInfo).message("视频播放地址已获取!");
    }
}
