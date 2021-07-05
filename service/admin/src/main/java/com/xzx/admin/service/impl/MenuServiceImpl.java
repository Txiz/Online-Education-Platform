package com.xzx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.admin.entity.Menu;
import com.xzx.admin.entity.RoleMenu;
import com.xzx.admin.entity.User;
import com.xzx.admin.mapper.MenuMapper;
import com.xzx.admin.mapper.RoleMenuMapper;
import com.xzx.admin.service.MenuService;
import com.xzx.admin.vo.MenuSearchVo;
import com.xzx.admin.vo.MenuSideVo;
import com.xzx.admin.vo.MenuTreeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 删除相关缓存
     *
     * @param userId 用户id
     */
    @Override
    public void deleteRedisKey(Integer userId) {
        redisTemplate.delete("menu_list");
        redisTemplate.delete("menu_total");
        redisTemplate.delete("menu_current");
        redisTemplate.delete("menu_size");
        redisTemplate.delete("menu_of_user_" + userId);
        redisTemplate.delete("menu_parent");
        redisTemplate.delete("menu_tree");
        redisTemplate.delete("role_list");
    }

    /**
     * 检查redis中key是否为空
     *
     * @param menuList    菜单列表
     * @param menuTotal   菜单总数
     * @param menuCurrent 当前页
     * @param menuSize    每页大小
     * @return 是否存在有key不存在
     */
    private boolean checkRedisKey(List<MenuTreeVo> menuList, Integer menuTotal, Integer menuCurrent, Integer menuSize) {
        return ObjectUtils.isEmpty(menuList) ||
                ObjectUtils.isEmpty(menuTotal) ||
                ObjectUtils.isEmpty(menuCurrent) ||
                ObjectUtils.isEmpty(menuSize);
    }

    /**
     * 分页查询菜单列表
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    @Override
    public Map<String, Object> pageMenu(Integer current, Integer size) {
        List<MenuTreeVo> menuList = (List<MenuTreeVo>) redisTemplate.opsForValue().get("menu_list");
        Integer menuTotal = (Integer) redisTemplate.opsForValue().get("menu_total");
        Integer menuCurrent = (Integer) redisTemplate.opsForValue().get("menu_current");
        Integer menuSize = (Integer) redisTemplate.opsForValue().get("menu_size");
        if (checkRedisKey(menuList, menuTotal, menuCurrent, menuSize) || !current.equals(menuCurrent) || !size.equals(menuSize)) {
            Page<Menu> menuPage = new Page<>(current, size);
            QueryWrapper<Menu> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", 0);
            wrapper.orderByDesc("sort");
            menuMapper.selectPage(menuPage, wrapper);
            List<Menu> menus = menuMapper.selectList(null);
            menuList = new ArrayList<>();
            for (Menu menu : menuPage.getRecords()) {
                MenuTreeVo menuTreeVo = new MenuTreeVo();
                BeanUtils.copyProperties(menu, menuTreeVo);
                List<MenuTreeVo> children = setTree(menus, menuTreeVo.getMenuId());
                menuTreeVo.setChildren(children);
                menuList.add(menuTreeVo);
            }
            menuTotal = Math.toIntExact(menuPage.getTotal());
            redisTemplate.opsForValue().set("menu_list", menuList);
            redisTemplate.opsForValue().set("menu_total", menuPage.getTotal());
            redisTemplate.opsForValue().set("menu_current", menuPage.getCurrent());
            redisTemplate.opsForValue().set("menu_size", menuPage.getSize());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", menuTotal);
        map.put("menus", menuList);
        return map;
    }

    /**
     * 模糊查询菜单
     *
     * @param current      当前页
     * @param size         每页大小
     * @param menuSearchVo 菜单查询条件视图
     * @return 结果集
     */
    @Override
    public Map<String, Object> searchMenu(Integer current, Integer size, MenuSearchVo menuSearchVo) {
        String menuName = menuSearchVo.getMenuName();
        Boolean isHidden = menuSearchVo.getIsHidden();
        String beginTime = menuSearchVo.getBeginTime();
        String endTime = menuSearchVo.getEndTime();
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(menuName)) wrapper.like("menu_name", menuName);
        if (!ObjectUtils.isEmpty(isHidden)) wrapper.eq("is_hidden", isHidden);
        if (!ObjectUtils.isEmpty(beginTime)) wrapper.ge("create_time", beginTime);
        if (!ObjectUtils.isEmpty(endTime)) wrapper.le("create_time", endTime);
        wrapper.eq("parent_id", 0);
        wrapper.orderByDesc("sort");
        Page<Menu> menuPage = new Page<>(current, size);
        menuMapper.selectPage(menuPage, wrapper);
        List<Menu> menus = menuMapper.selectList(null);
        List<MenuTreeVo> menuList = new ArrayList<>();
        for (Menu menu : menuPage.getRecords()) {
            MenuTreeVo menuTreeVo = new MenuTreeVo();
            BeanUtils.copyProperties(menu, menuTreeVo);
            List<MenuTreeVo> children = setTree(menus, menuTreeVo.getMenuId());
            menuTreeVo.setChildren(children);
            menuList.add(menuTreeVo);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", menuPage.getTotal());
        map.put("menus", menuList);
        return map;
    }

    /**
     * 根据菜单id删除菜单
     *
     * @param menuId 菜单id
     * @return 是否删除成功
     */
    @Override
    public boolean deleteMenuById(Integer menuId) {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", menuId);
        List<Menu> menus = menuMapper.selectList(wrapper);
        if (menus.size() <= 0) {
            QueryWrapper<RoleMenu> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("menu_id", menuId);
            roleMenuMapper.delete(wrapper1);
            menuMapper.deleteById(menuId);
            return true;
        }
        for (Menu menu : menus) {
            deleteMenuById(menu.getMenuId());
        }
        QueryWrapper<RoleMenu> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("menu_id", menuId);
        roleMenuMapper.delete(wrapper2);
        return menuMapper.deleteById(menuId) > 0;
    }

    /**
     * 根据用户id查询菜单栏
     *
     * @return 菜单栏视图列表
     */
    @Override
    public List<MenuSideVo> getMenusByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((User) authentication.getPrincipal()).getUserId();
        List<MenuSideVo> sideMenuVos = (List<MenuSideVo>) redisTemplate.opsForValue().get("menu_of_user_" + userId);
        if (ObjectUtils.isEmpty(sideMenuVos)) {
            sideMenuVos = menuMapper.getMenusByUserId(userId);
            sideMenuVos = setChildren(sideMenuVos, 0);
            redisTemplate.opsForValue().set("menu_of_user_" + userId, sideMenuVos);
        }
        return sideMenuVos;
    }

    /**
     * 查询所有一级菜单
     *
     * @return 菜单列表
     */
    @Override
    public List<Menu> listAllParentMenu() {
        List<Menu> menuList = (List<Menu>) redisTemplate.opsForValue().get("menu_parent");
        if (ObjectUtils.isEmpty(menuList)) {
            QueryWrapper<Menu> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", 0);
            wrapper.orderByDesc("sort");
            menuList = menuMapper.selectList(wrapper);
            redisTemplate.opsForValue().set("menu_parent", menuList);
        }
        return menuList;
    }

    /**
     * 查询菜单树
     *
     * @return 菜单树视图列表
     */
    @Override
    public List<MenuTreeVo> listMenuTree() {
        List<MenuTreeVo> menuTreeVos = (List<MenuTreeVo>) redisTemplate.opsForValue().get("menu_tree");
        if (ObjectUtils.isEmpty(menuTreeVos)) {
            QueryWrapper<Menu> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("sort");
            List<Menu> menus = menuMapper.selectList(wrapper);
            menuTreeVos = setTree(menus, 0);
            redisTemplate.opsForValue().set("menu_tree", menuTreeVos);
        }
        return menuTreeVos;
    }

    /**
     * 递归方法，构造递归侧边菜单栏
     *
     * @param menuSideVos 菜单列表
     * @param parentId    父菜单id
     * @return 菜单栏视图列表
     */
    private List<MenuSideVo> setChildren(List<MenuSideVo> menuSideVos, Integer parentId) {
        List<MenuSideVo> res = new ArrayList<>();
        for (MenuSideVo menuSideVo : menuSideVos) {
            if (menuSideVo.getParentId().equals(parentId)) {
                List<MenuSideVo> children = setChildren(menuSideVos, menuSideVo.getMenuId());
                menuSideVo.setChildren(children);
                res.add(menuSideVo);
            }
        }
        return res;
    }

    /**
     * 递归方法，构造递归菜单树
     *
     * @param menus    菜单列表
     * @param parentId 父菜单id
     * @return 菜单树视图列表
     */
    private List<MenuTreeVo> setTree(List<Menu> menus, Integer parentId) {
        List<MenuTreeVo> res = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getParentId().equals(parentId)) {
                MenuTreeVo menuTreeVo = new MenuTreeVo();
                BeanUtils.copyProperties(menu, menuTreeVo);
                List<MenuTreeVo> children = setTree(menus, menuTreeVo.getMenuId());
                menuTreeVo.setChildren(children);
                res.add(menuTreeVo);
            }
        }
        return res;
    }
}
