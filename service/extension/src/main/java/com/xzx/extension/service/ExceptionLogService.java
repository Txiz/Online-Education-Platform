package com.xzx.extension.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.common.result.R;
import com.xzx.extension.entity.ExceptionLog;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xzx
 * @since 2021-03-28
 */
public interface ExceptionLogService extends IService<ExceptionLog> {

    /**
     * 保存异常日志
     *
     * @param exceptionLog 异常日志数据
     * @return 统一结果封装
     */
    R saveExceptionLog(ExceptionLog exceptionLog);

    /**
     * 分页查询异常日志
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 统一结果封装
     */
    R pageExceptionLog(Integer current, Integer size);

    /**
     * 根据id删除异常日志
     *
     * @param logId 异常日志id
     * @return 统一结果封装
     */
    R deleteByLogId(Integer logId);
}
