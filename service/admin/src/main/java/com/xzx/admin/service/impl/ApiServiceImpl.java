package com.xzx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.admin.dto.ApiDto;
import com.xzx.admin.entity.Api;
import com.xzx.admin.entity.Role;
import com.xzx.admin.entity.RoleApi;
import com.xzx.admin.mapper.ApiMapper;
import com.xzx.admin.mapper.RoleApiMapper;
import com.xzx.admin.mapper.RoleMapper;
import com.xzx.admin.service.ApiService;
import com.xzx.admin.vo.ApiSearchVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
@Service
public class ApiServiceImpl extends ServiceImpl<ApiMapper, Api> implements ApiService {

    @Resource
    private ApiMapper apiMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleApiMapper roleApiMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 删除相关缓存
     */
    @Override
    public void deleteRedisKey() {
        redisTemplate.delete("api_list");
        redisTemplate.delete("api_total");
        redisTemplate.delete("api_current");
        redisTemplate.delete("api_size");
        redisTemplate.delete("api_auth_list");
        redisTemplate.delete("api_dto_list");
        redisTemplate.delete("role_list");
    }

    /**
     * 检查redis中key是否为空
     *
     * @param apiList    接口列表
     * @param apiTotal   接口总数
     * @param apiCurrent 当前页
     * @param apiSize    每页大小
     * @return 是否存在有key不存在
     */
    private boolean checkRedisKey(List<Api> apiList, Integer apiTotal, Integer apiCurrent, Integer apiSize) {
        return ObjectUtils.isEmpty(apiList) ||
                ObjectUtils.isEmpty(apiTotal) ||
                ObjectUtils.isEmpty(apiCurrent) ||
                ObjectUtils.isEmpty(apiSize);
    }

    /**
     * 分页查询所有接口
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    @Override
    public Map<String, Object> pageApi(Integer current, Integer size) {
        List<Api> apiList = (List<Api>) redisTemplate.opsForValue().get("api_list");
        Integer apiTotal = (Integer) redisTemplate.opsForValue().get("api_total");
        Integer apiCurrent = (Integer) redisTemplate.opsForValue().get("api_current");
        Integer apiSize = (Integer) redisTemplate.opsForValue().get("api_size");
        if (checkRedisKey(apiList, apiTotal, apiCurrent, apiSize) || !current.equals(apiCurrent) || !size.equals(apiSize)) {
            Page<Api> apiPage = new Page<>(current, size);
            apiMapper.selectPage(apiPage, null);
            apiList = apiPage.getRecords();
            apiTotal = Math.toIntExact(apiPage.getTotal());
            redisTemplate.opsForValue().set("api_list", apiPage.getRecords());
            redisTemplate.opsForValue().set("api_total", apiTotal);
            redisTemplate.opsForValue().set("api_current", apiPage.getCurrent());
            redisTemplate.opsForValue().set("api_size", apiPage.getSize());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", apiTotal);
        map.put("apiList", apiList);
        return map;
    }

    /**
     * 模糊查询接口
     *
     * @param current     当前页
     * @param size        每页大小
     * @param apiSearchVo 接口查询条件视图
     * @return 结果集
     */
    @Override
    public Map<String, Object> searchApi(Integer current, Integer size, ApiSearchVo apiSearchVo) {
        String apiName = apiSearchVo.getApiName();
        String apiUrl = apiSearchVo.getApiUrl();
        Boolean isAuth = apiSearchVo.getIsAuth();
        String beginTime = apiSearchVo.getBeginTime();
        String endTime = apiSearchVo.getEndTime();
        QueryWrapper<Api> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(apiName)) wrapper.like("api_name", apiName);
        if (!ObjectUtils.isEmpty(apiUrl)) wrapper.like("api_url", apiUrl);
        if (!ObjectUtils.isEmpty(isAuth)) wrapper.eq("is_auth", isAuth);
        if (!ObjectUtils.isEmpty(beginTime)) wrapper.ge("create_time", beginTime);
        if (!ObjectUtils.isEmpty(endTime)) wrapper.le("create_time", endTime);
        wrapper.orderByDesc("api_url");
        Page<Api> apiPage = new Page<>(current, size);
        apiMapper.selectPage(apiPage, wrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("total", apiPage.getTotal());
        map.put("apiList", apiPage.getRecords());
        return map;
    }

    /**
     * 根据接口id删除接口
     *
     * @param apiId 接口id
     * @return 是否删除成功
     */
    @Override
    public boolean deleteApiById(Integer apiId) {
        QueryWrapper<RoleApi> wrapper = new QueryWrapper<>();
        wrapper.eq("api_id", apiId);
        roleApiMapper.delete(wrapper);
        return apiMapper.deleteById(apiId) > 0;
    }

    /**
     * 查询所有需要授权的接口
     *
     * @return 接口列表
     */
    @Override
    public List<Api> listAuthApi() {
        List<Api> authApiList = (List<Api>) redisTemplate.opsForValue().get("api_auth_list");
        if (ObjectUtils.isEmpty(authApiList)) {
            QueryWrapper<Api> wrapper = new QueryWrapper<>();
            wrapper.eq("is_auth", 1);
            authApiList = apiMapper.selectList(wrapper);
            redisTemplate.opsForValue().set("api_auth_list", authApiList);
        }
        return authApiList;
    }

    /**
     * 查询每个接口所需要的角色
     *
     * @return 接口传输对象
     */
    @Override
    public List<ApiDto> getApisWithRole() {
        List<ApiDto> apiDtoList = (List<ApiDto>) redisTemplate.opsForValue().get("api_dto_list");
        if (ObjectUtils.isEmpty(apiDtoList)) {
            List<Api> apis = apiMapper.selectList(null);
            apiDtoList = new ArrayList<>();
            for (Api api : apis) {
                ApiDto apiDto = new ApiDto();
                BeanUtils.copyProperties(api, apiDto);
                List<Role> roles = roleMapper.getRolesByApiId(apiDto.getApiId());
                apiDto.setRoles(roles);
                apiDtoList.add(apiDto);
            }
            redisTemplate.opsForValue().set("api_dto_list", apiDtoList);
        }
        return apiDtoList;
    }
}
