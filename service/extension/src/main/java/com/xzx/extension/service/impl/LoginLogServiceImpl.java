package com.xzx.extension.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.common.result.R;
import com.xzx.extension.entity.LoginLog;
import com.xzx.extension.mapper.LoginLogMapper;
import com.xzx.extension.service.LoginLogService;
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
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    /**
     * 保存登录日志
     *
     * @param loginLog 登录日志数据
     * @return 统一结果封装
     */
    @Override
    public R saveLoginLog(LoginLog loginLog) {
        save(loginLog);
        return R.ok().message("保存登录日志成功!");
    }

    /**
     * 分页查询登录日志
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    @Override
    public R pageLoginLog(Integer current, Integer size) {
        Page<LoginLog> loginLogPage = new Page<>(current, size);
        page(loginLogPage);
        Map<String, Object> map = new HashMap<>();
        map.put("total", loginLogPage.getTotal());
        map.put("logList", loginLogPage.getRecords());
        return R.ok().data(map).message("分页查询登录日志成功!");
    }

    /**
     * 根据id删除登录日志
     *
     * @param logId 登录日志id
     * @return 统一结果封装
     */
    @Override
    public R deleteByLogId(Integer logId) {
        return removeById(logId) ? R.ok().message("登录日志删除成功!") : R.error().message("登录日志删除失败!");
    }
}
