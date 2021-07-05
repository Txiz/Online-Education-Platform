package com.xzx.admin.service.impl;

import com.xzx.admin.service.IndexService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 入口服务实现
 * 作者: xzx
 * 创建时间: 2021-03-19-09-48
 **/
@Service
public class IndexServiceImpl implements IndexService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 删除相关缓存
     *
     * @param userId 用户id
     */
    @Override
    public void deleteRedisKey(Integer userId) {
        redisTemplate.delete("menu_of_user_" + userId);
        redisTemplate.delete("role_of_user_" + userId);
    }
}
