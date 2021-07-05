package com.xzx.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzx.admin.entity.Role;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id获取所有角色
     *
     * @param userId 用户id
     * @return 角色列表
     */
    List<Role> getRolesByUserId(Integer userId);

    /**
     * 根据接口id获取所有的角色
     *
     * @param apiId 接口id
     * @return 角色列表
     */
    List<Role> getRolesByApiId(Integer apiId);
}
