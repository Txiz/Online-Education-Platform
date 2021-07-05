package com.xzx.extension.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.common.result.R;
import com.xzx.extension.entity.OperateLog;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xzx
 * @since 2021-03-28
 */
public interface OperateLogService extends IService<OperateLog> {

    /**
     * 保存操作日志
     *
     * @param operateLog 操作日志数据
     * @return 统一结果封装
     */
    R saveOperateLog(OperateLog operateLog);

    /**
     * 分页查询操作日志
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    R pageOperateLog(Integer current, Integer size);

    /**
     * 根据id删除操作日志
     *
     * @param logId 操作日志id
     * @return 统一结果封装
     */
    R deleteByLogId(Integer logId);
}
