package com.xzx.extension.controller;


import com.xzx.common.result.R;
import com.xzx.extension.entity.ExceptionLog;
import com.xzx.extension.service.ExceptionLogService;
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
 * @since 2021-03-28
 */
@RestController
@RequestMapping("/extension/exception-log")
@Api(tags = "异常日志控制器")
public class ExceptionLogController {

    @Resource
    private ExceptionLogService exceptionLogService;

    @ApiOperation(value = "保存异常日志")
    @PostMapping("/saveExceptionLog")
    public R saveExceptionLog(@RequestBody ExceptionLog exceptionLog) {
        return exceptionLogService.saveExceptionLog(exceptionLog);
    }

    @ApiOperation(value = "分页查询异常日志")
    @PostMapping("/pageExceptionLog/{current}/{size}")
    public R pageExceptionLog(@PathVariable Integer current, @PathVariable Integer size) {
        return exceptionLogService.pageExceptionLog(current, size);
    }

    @ApiOperation(value = "删除异常日志")
    @DeleteMapping("/deleteByLogId/{logId}")
    public R deleteByLogId(@PathVariable Integer logId) {
        return exceptionLogService.deleteByLogId(logId);
    }
}

