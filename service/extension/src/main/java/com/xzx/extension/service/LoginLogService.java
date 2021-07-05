package com.xzx.extension.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.common.result.R;
import com.xzx.extension.entity.LoginLog;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xzx
 * @since 2021-03-28
 */
public interface LoginLogService extends IService<LoginLog> {

    /**
     * 保存登录日志
     *
     * @param loginLog 登录日志数据
     * @return 统一结果封装
     */
    R saveLoginLog(LoginLog loginLog);

    /**
     * 分页查询登录日志
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    R pageLoginLog(Integer current, Integer size);

    /**
     * 根据id删除登录日志
     *
     * @param logId 登录日志id
     * @return 统一结果封装
     */
    R deleteByLogId(Integer logId);
}
