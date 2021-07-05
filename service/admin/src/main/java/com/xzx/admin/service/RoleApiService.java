package com.xzx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.admin.entity.RoleApi;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
public interface RoleApiService extends IService<RoleApi> {

    /**
     * 保存角色接口列表
     *
     * @param roleId 角色id
     * @param apiIds 接口id列表
     */
    void saveRoleApis(Integer roleId, List<Integer> apiIds);
}
