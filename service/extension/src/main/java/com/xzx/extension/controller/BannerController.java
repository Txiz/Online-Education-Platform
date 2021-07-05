package com.xzx.extension.controller;


import com.xzx.common.result.R;
import com.xzx.extension.entity.Banner;
import com.xzx.extension.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xzx
 * @since 2021-03-29
 */
@RestController
@RequestMapping("/extension/banner")
@Api(tags = "轮播图控制器")
public class BannerController {

    @Resource
    private BannerService bannerService;

    @Transactional
    @ApiOperation(value = "保存轮播图")
    @PostMapping("/saveBanner")
    public R saveBanner(@RequestBody Banner banner) {
        return bannerService.saveBanner(banner);
    }

    @ApiOperation(value = "分页查询轮播图")
    @PostMapping("/pageBanner/{current}/{size}")
    public R pageBanner(@PathVariable Integer current, @PathVariable Integer size) {
        return bannerService.pageBanner(current, size);
    }

    @ApiOperation(value = "根据id删除轮播图")
    @DeleteMapping("/deleteById/{bannerId}")
    public R deleteById(@PathVariable Integer bannerId) {
        return bannerService.deleteById(bannerId);
    }

    @ApiOperation(value = "前台方法——查询轮播图列表")
    @GetMapping("/listBanner")
    public R listBanner() {
        return bannerService.listBanner();
    }
}

