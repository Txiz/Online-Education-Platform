package com.xzx.extension.controller;


import com.xzx.common.result.R;
import com.xzx.extension.entity.LoginLog;
import com.xzx.extension.service.LoginLogService;
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
@RequestMapping("/extension/login-log")
@Api(tags = "登录日志控制器")
public class LoginLogController {

    @Resource
    private LoginLogService loginLogService;

    @ApiOperation(value = "保存登录日志")
    @PostMapping("/saveLoginLog")
    public R saveLoginLog(@RequestBody LoginLog loginLog) {
        return loginLogService.saveLoginLog(loginLog);
    }

    @ApiOperation(value = "分页查询登录日志")
    @PostMapping("/pageLoginLog/{current}/{size}")
    public R pageLoginLog(@PathVariable Integer current, @PathVariable Integer size) {
        return loginLogService.pageLoginLog(current, size);
    }

    @ApiOperation(value = "删除登录日志")
    @DeleteMapping("/deleteByLogId/{logId}")
    public R deleteByLogId(@PathVariable Integer logId) {
        return loginLogService.deleteByLogId(logId);
    }
}

