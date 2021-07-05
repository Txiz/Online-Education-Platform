package com.xzx.admin.controller;


import com.xzx.admin.annotation.OperateLogger;
import com.xzx.admin.entity.Menu;
import com.xzx.admin.entity.User;
import com.xzx.admin.service.MenuService;
import com.xzx.admin.vo.MenuSearchVo;
import com.xzx.common.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
@RestController
@RequestMapping("/admin/menu")
@Api(tags = "系统菜单控制器")
public class MenuController {

    @Resource
    private MenuService menuService;

    @Transactional
    @OperateLogger(description = "保存或者更新菜单")
    @ApiOperation(value = "保存或者更新菜单")
    @PostMapping("/saveOrUpdateMenu")
    public R saveOrUpdateMenu(@RequestBody Menu menu) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((User) authentication.getPrincipal()).getUserId();
        if (menu == null)
            return R.error().message("菜单信息不能全部为空");
        menuService.saveOrUpdate(menu);
        menuService.deleteRedisKey(userId);
        return R.ok().message("保存或更新菜单成功!");
    }

    @OperateLogger(description = "分页查询菜单列表")
    @ApiOperation(value = "分页查询菜单列表")
    @GetMapping("/pageMenu/{current}/{size}")
    public R pageMenu(@PathVariable Integer current, @PathVariable Integer size) {
        return R.ok().data(menuService.pageMenu(current, size)).message("分页查询菜单列表成功!");
    }

    @OperateLogger(description = "模糊搜索菜单")
    @ApiOperation(value = "模糊搜索菜单")
    @PostMapping("/searchMenu/{current}/{size}")
    public R searchMenu(@PathVariable Integer current, @PathVariable Integer size, @RequestBody(required = false) MenuSearchVo menuSearchVo) {
        if (ObjectUtils.isEmpty(menuSearchVo)) {
            return R.error().message("请输入查询条件!");
        }
        return R.ok().data(menuService.searchMenu(current, size, menuSearchVo)).message("搜索成功!");
    }

    @Transactional
    @OperateLogger(description = "根据id删除菜单")
    @ApiOperation(value = "根据id删除菜单")
    @DeleteMapping("/deleteMenuById/{menuId}")
    public R deleteMenuById(@PathVariable Integer menuId) {
        // 获得当前用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((User) authentication.getPrincipal()).getUserId();
        menuService.deleteRedisKey(userId);
        return menuService.deleteMenuById(menuId) ? R.ok().message("删除菜单成功!") : R.error().message("删除菜单失败!");
    }

    @OperateLogger(description = "通过当前登录用户id查询菜单列表")
    @ApiOperation(value = "通过当前登录用户id查询菜单列表")
    @GetMapping("/getMenusByUserId")
    public R getMenusByUserId() {
        return R.ok().data("menuList", menuService.getMenusByUserId()).message("查询当前用户的所有菜单成功!");
    }

    @OperateLogger(description = "查询所有父菜单")
    @ApiOperation(value = "查询所有父菜单")
    @GetMapping("/listAllParentMenu")
    public R listAllParentMenu() {
        return R.ok().data("parentMenus", menuService.listAllParentMenu()).message("查询所有父菜单成功!");
    }

    @OperateLogger(description = "查询用于角色分配的菜单树")
    @ApiOperation(value = "查询用于角色分配的菜单树")
    @GetMapping("/listMenuTree")
    public R listMenuTree() {
        return R.ok().data("menuTree", menuService.listMenuTree()).message("查询菜单树成功!");
    }
}

