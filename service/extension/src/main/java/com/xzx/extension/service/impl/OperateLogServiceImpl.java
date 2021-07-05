package com.xzx.extension.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.common.result.R;
import com.xzx.extension.entity.OperateLog;
import com.xzx.extension.mapper.OperateLogMapper;
import com.xzx.extension.service.OperateLogService;
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
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements OperateLogService {

    /**
     * 保存操作日志
     *
     * @param operateLog 操作日志数据
     * @return 统一结果封装
     */
    @Override
    public R saveOperateLog(OperateLog operateLog) {
        save(operateLog);
        return R.ok().message("保存操作日志成功!");
    }

    /**
     * 分页查询操作日志
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 统一结果封装
     */
    @Override
    public R pageOperateLog(Integer current, Integer size) {
        Page<OperateLog> operateLogPage = new Page<>(current, size);
        page(operateLogPage);
        Map<String, Object> map = new HashMap<>();
        map.put("total", operateLogPage.getTotal());
        map.put("logList", operateLogPage.getRecords());
        return R.ok().data(map).message("分页查询操作日志!");
    }

    /**
     * 根据id删除操作日志
     *
     * @param logId 操作日志id
     * @return 统一结果封装
     */
    @Override
    public R deleteByLogId(Integer logId) {
        return removeById(logId) ? R.ok().message("操作日志删除成功!") : R.error().message("操作日志删除失败!");
    }
}
