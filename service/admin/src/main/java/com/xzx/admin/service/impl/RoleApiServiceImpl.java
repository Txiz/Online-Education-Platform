package com.xzx.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.admin.entity.RoleApi;
import com.xzx.admin.mapper.RoleApiMapper;
import com.xzx.admin.service.RoleApiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
@Service
public class RoleApiServiceImpl extends ServiceImpl<RoleApiMapper, RoleApi> implements RoleApiService {

    @Resource
    private RoleApiMapper roleApiMapper;

    /**
     * 保存角色接口列表
     *
     * @param roleId 角色id
     * @param apiIds 接口id列表
     */
    @Override
    public void saveRoleApis(Integer roleId, List<Integer> apiIds) {
        RoleApi roleApi = new RoleApi();
        roleApi.setRoleId(roleId);
        for (Integer apiId : apiIds) {
            roleApi.setApiId(apiId);
            roleApiMapper.insert(roleApi);
        }
    }
}
