package com.xzx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.admin.dto.ApiDto;
import com.xzx.admin.entity.Api;
import com.xzx.admin.vo.ApiSearchVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
public interface ApiService extends IService<Api> {

    /**
     * 删除相关缓存
     */
    void deleteRedisKey();

    /**
     * 分页查询所有接口
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    Map<String, Object> pageApi(Integer current, Integer size);

    /**
     * 模糊查询接口
     *
     * @param current     当前页
     * @param size        每页大小
     * @param apiSearchVo 接口查询条件视图
     * @return 结果集
     */
    Map<String, Object> searchApi(Integer current, Integer size, ApiSearchVo apiSearchVo);

    /**
     * 根据接口id删除接口
     *
     * @param apiId 接口id
     * @return 是否删除成功
     */
    boolean deleteApiById(Integer apiId);

    /**
     * 查询所有需要授权的接口
     *
     * @return 接口列表
     */
    List<Api> listAuthApi();

    /**
     * 查询每个接口所需要的角色
     *
     * @return 接口传输对象
     */
    List<ApiDto> getApisWithRole();
}
