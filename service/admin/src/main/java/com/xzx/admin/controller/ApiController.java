package com.xzx.admin.controller;


import com.xzx.admin.annotation.OperateLogger;
import com.xzx.admin.entity.Api;
import com.xzx.admin.service.ApiService;
import com.xzx.admin.vo.ApiSearchVo;
import com.xzx.common.result.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
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
@RequestMapping("/admin/api")
@io.swagger.annotations.Api(tags = "系统接口控制器")
public class ApiController {

    @Resource
    private ApiService apiService;

    @Transactional
    @OperateLogger(description = "保存或者更新接口")
    @ApiOperation(value = "保存或者更新接口")
    @PostMapping("/saveOrUpdateApi")
    public R saveOrUpdateApi(@RequestBody Api api) {
        if (api == null)
            return R.error().message("接口信息不能全部为空");
        apiService.saveOrUpdate(api);
        apiService.deleteRedisKey();
        return R.ok().message("保存或更新接口成功!");
    }

    @OperateLogger(description = "分页查询所有接口")
    @ApiOperation(value = "分页查询所有接口")
    @GetMapping("/pageApi/{current}/{size}")
    public R pageApi(@PathVariable Integer current, @PathVariable Integer size) {
        return R.ok().data(apiService.pageApi(current, size)).message("分页查询所有接口成功!");
    }

    @OperateLogger(description = "模糊搜索接口")
    @ApiOperation(value = "模糊搜索接口")
    @PostMapping("/searchApi/{current}/{size}")
    public R searchApi(@PathVariable Integer current, @PathVariable Integer size, @RequestBody(required = false) ApiSearchVo apiSearchVo) {
        if (ObjectUtils.isEmpty(apiSearchVo)) {
            return R.error().message("请输入查询条件!");
        }
        return R.ok().data(apiService.searchApi(current, size, apiSearchVo)).message("搜索成功!");
    }

    @Transactional
    @OperateLogger(description = "根据id删除接口")
    @ApiOperation(value = "根据id删除接口")
    @DeleteMapping("/deleteApiById/{apiId}")
    public R deleteApiById(@PathVariable Integer apiId) {
        apiService.deleteRedisKey();
        return apiService.deleteApiById(apiId) ? R.ok().message("删除接口成功!") : R.error().message("删除接口失败!");
    }

    @OperateLogger(description = "查询所有需要授权的接口")
    @ApiOperation(value = "查询所有需要授权的接口")
    @GetMapping("/listAuthApi")
    public R listAuthApi() {
        return R.ok().data("apiAuthList", apiService.listAuthApi()).message("查询所有需要授权的接口成功!");
    }
}

