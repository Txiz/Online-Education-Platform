package com.xzx.extension.controller;


import com.xzx.common.result.R;
import com.xzx.extension.entity.OperateLog;
import com.xzx.extension.service.OperateLogService;
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
@RequestMapping("/extension/operate-log")
@Api(tags = "操作日志控制器")
public class OperateLogController {

    @Resource
    private OperateLogService operateLogService;

    @ApiOperation(value = "保存操作日志")
    @PostMapping("/saveOperateLog")
    public R saveOperateLog(@RequestBody OperateLog operateLog) {
        return operateLogService.saveOperateLog(operateLog);
    }

    @ApiOperation(value = "分页查询操作日志")
    @PostMapping("/pageOperateLog/{current}/{size}")
    public R pageOperateLog(@PathVariable Integer current, @PathVariable Integer size) {
        return operateLogService.pageOperateLog(current, size);
    }

    @ApiOperation(value = "删除操作日志")
    @DeleteMapping("/deleteByLogId/{logId}")
    public R deleteByLogId(@PathVariable Integer logId) {
        return operateLogService.deleteByLogId(logId);
    }
}

