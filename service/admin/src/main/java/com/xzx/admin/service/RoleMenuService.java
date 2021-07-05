package com.xzx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.admin.entity.RoleMenu;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
public interface RoleMenuService extends IService<RoleMenu> {

    /**
     * 保存角色菜单列表
     *
     * @param roleId  角色id
     * @param menuIds 菜单id列表
     */
    void saveRoleMenus(Integer roleId, List<Integer> menuIds);
}
