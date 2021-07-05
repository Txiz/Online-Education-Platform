package com.xzx.extension.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.common.result.R;
import com.xzx.extension.entity.ExceptionLog;
import com.xzx.extension.mapper.ExceptionLogMapper;
import com.xzx.extension.service.ExceptionLogService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xzx
 * @since 2021-03-28
 */
@Service
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog> implements ExceptionLogService {

    /**
     * 保存异常日志
     *
     * @param exceptionLog 异常日志数据
     * @return 统一结果封装
     */
    @Override
    public R saveExceptionLog(ExceptionLog exceptionLog) {
        save(exceptionLog);
        return R.ok().message("保存异常日志成功!");
    }

    /**
     * 分页查询异常日志
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 统一结果封装
     */
    @Override
    public R pageExceptionLog(Integer current, Integer size) {
        Page<ExceptionLog> exceptionLogPage = new Page<>(current, size);
        page(exceptionLogPage);
        Map<String, Object> map = new HashMap<>();
        map.put("total", exceptionLogPage.getTotal());
        map.put("logList", exceptionLogPage.getRecords());
        return R.ok().data(map).message("分页查询异常日志成功!");
    }


    /**
     * 根据id删除异常日志
     *
     * @param logId 异常日志id
     * @return 统一结果封装
     */
    @Override
    public R deleteByLogId(Integer logId) {
        return removeById(logId) ? R.ok().message("登录日志删除成功!") : R.error().message("登录日志删除失败!");
    }
}
