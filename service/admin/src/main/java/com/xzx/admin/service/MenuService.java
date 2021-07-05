package com.xzx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.admin.entity.Menu;
import com.xzx.admin.vo.MenuSearchVo;
import com.xzx.admin.vo.MenuSideVo;
import com.xzx.admin.vo.MenuTreeVo;

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
public interface MenuService extends IService<Menu> {

    /**
     * 删除相关缓存
     *
     * @param userId 用户id
     */
    void deleteRedisKey(Integer userId);

    /**
     * 分页查询菜单列表
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    Map<String, Object> pageMenu(Integer current, Integer size);

    /**
     * 模糊查询菜单
     *
     * @param current      当前页
     * @param size         每页大小
     * @param menuSearchVo 菜单查询条件视图
     * @return 结果集
     */
    Map<String, Object> searchMenu(Integer current, Integer size, MenuSearchVo menuSearchVo);

    /**
     * 根据菜单id删除菜单
     *
     * @param menuId 菜单id
     * @return 是否删除成功
     */
    boolean deleteMenuById(Integer menuId);

    /**
     * 根据用户id查询菜单栏
     *
     * @return 菜单栏视图列表
     */
    List<MenuSideVo> getMenusByUserId();

    /**
     * 查询所有一级菜单
     *
     * @return 菜单列表
     */
    List<Menu> listAllParentMenu();

    /**
     * 查询菜单树
     *
     * @return 菜单树视图列表
     */
    List<MenuTreeVo> listMenuTree();
}
