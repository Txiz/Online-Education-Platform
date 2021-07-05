package com.xzx.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.admin.entity.UserRole;
import com.xzx.admin.mapper.UserRoleMapper;
import com.xzx.admin.service.UserRoleService;
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
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 保存用户角色关系
     *
     * @param userId  用户id
     * @param roleIds 角色id列表
     */
    @Override
    public void saveUserRoles(Integer userId, List<Integer> roleIds) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        for (Integer roleId : roleIds) {
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }
    }
}
