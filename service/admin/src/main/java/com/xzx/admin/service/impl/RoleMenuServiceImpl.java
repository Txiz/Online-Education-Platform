package com.xzx.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.admin.entity.RoleMenu;
import com.xzx.admin.mapper.RoleMenuMapper;
import com.xzx.admin.service.RoleMenuService;
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
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    /**
     * 保存角色菜单列表
     *
     * @param roleId  角色id
     * @param menuIds 菜单id列表
     */
    @Override
    public void saveRoleMenus(Integer roleId, List<Integer> menuIds) {
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(roleId);
        for (Integer menuId : menuIds) {
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }
}
