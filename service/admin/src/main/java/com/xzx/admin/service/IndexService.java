package com.xzx.admin.service;

/**
 * 入口服务接口
 * 作者: xzx
 * 创建时间: 2021-03-19-09-48
 **/
public interface IndexService {

    /**
     * 删除相关缓存
     *
     * @param userId 用户id
     */
    void deleteRedisKey(Integer userId);
}
