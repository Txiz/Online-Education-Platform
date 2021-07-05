package com.xzx.extension.controller;


import com.xzx.common.result.R;
import com.xzx.extension.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xzx
 * @since 2021-04-12
 */
@RestController
@RequestMapping("/extension/statistics")
@Api(tags = "统计控制器")
public class StatisticsController {

    @Resource
    private StatisticsService statisticsService;

    @ApiOperation(value = "分页查询统计数据")
    @PostMapping("/pageStatistics/{current}/{size}")
    public R pageStatistics(@PathVariable Integer current, @PathVariable Integer size) {
        return statisticsService.pageStatistics(current, size);
    }

    @ApiOperation(value = "根据id删除统计数据")
    @DeleteMapping("/deleteById/{statisticsId}")
    public R deleteById(@PathVariable Integer statisticsId) {
        return statisticsService.deleteById(statisticsId);
    }

    @ApiOperation(value = "获取统计数据用于制作地图")
    @GetMapping("/getStatisticsDataForMap")
    public R getStatisticsDataForMap() {
        return statisticsService.getStatisticsDataForMap();
    }

    @ApiOperation(value = "从redis中获取当日临时统计数据")
    @GetMapping("/getCurrentStatisticsNum")
    public R getCurrentStatisticsNum() {
        return statisticsService.getCurrentStatisticsNum();
    }
}

