package com.xzx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.admin.entity.UserRole;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 保存用户角色关系
     *
     * @param userId  用户id
     * @param roleIds 角色id列表
     */
    void saveUserRoles(Integer userId, List<Integer> roleIds);
}
