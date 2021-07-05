package com.xzx.extension.controller;

import com.xzx.common.result.R;
import com.xzx.extension.service.ElasticSearchService;
import com.xzx.extension.vo.SearchParamVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 搜索控制器
 * 作者: xzx
 * 创建时间: 2021-04-11-10-54
 **/
@RestController
@RequestMapping("/extension/elasticsearch")
@Api(tags = "搜索控制器")
public class ElasticSearchController {

    @Resource
    private ElasticSearchService elasticSearchService;

    @ApiOperation(value = "搜索课程卡片视图列表")
    @PostMapping("/searchCourseCardVo")
    public R searchCourseCardVo(@RequestBody(required = false) SearchParamVo searchParamVo) {
        return elasticSearchService.searchCourseCardVo(searchParamVo);
    }

    @ApiOperation(value = "获取最新课程")
    @GetMapping("/getNewCourseCardVo")
    public R getNewCourseCardVo() {
        return elasticSearchService.getNewCourseCardVo();
    }
}
