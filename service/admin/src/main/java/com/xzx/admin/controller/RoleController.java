package com.xzx.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xzx.admin.annotation.OperateLogger;
import com.xzx.admin.entity.Role;
import com.xzx.admin.entity.RoleApi;
import com.xzx.admin.entity.RoleMenu;
import com.xzx.admin.entity.User;
import com.xzx.admin.service.RoleApiService;
import com.xzx.admin.service.RoleMenuService;
import com.xzx.admin.service.RoleService;
import com.xzx.admin.vo.RoleSearchVo;
import com.xzx.admin.vo.RoleVo;
import com.xzx.common.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/admin/role")
@Api(tags = "系统角色控制器")
public class RoleController {

    @Resource
    private RoleService roleService;

    @Resource
    private RoleApiService roleApiService;

    @Resource
    private RoleMenuService roleMenuService;

    @Transactional
    @OperateLogger(description = "保存或者更新角色")
    @ApiOperation(value = "保存或者更新角色")
    @PostMapping("/saveOrUpdateRole")
    public R saveOrUpdateRole(@RequestBody RoleVo roleVo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentUserId = ((User) authentication.getPrincipal()).getUserId();
        if (roleVo == null)
            return R.error().message("角色信息不能全部为空");
        Integer roleId = roleVo.getRoleId();
        Role role = new Role();
        BeanUtils.copyProperties(roleVo, role);
        if (roleId != null) {
            QueryWrapper<RoleApi> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("role_id", roleId);
            roleApiService.remove(wrapper1);
            QueryWrapper<RoleMenu> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("role_id", roleId);
            roleMenuService.remove(wrapper2);
        }
        roleService.saveOrUpdate(role);
        roleApiService.saveRoleApis(role.getRoleId(), roleVo.getApiIds());
        roleMenuService.saveRoleMenus(role.getRoleId(), roleVo.getMenuIds());
        roleService.deleteRedisKey(currentUserId);
        return R.ok().message("保存或者更新角色成功!");
    }

    @OperateLogger(description = "分页查询角色列表")
    @ApiOperation(value = "分页查询角色列表")
    @GetMapping("/pageRole/{current}/{size}")
    public R pageRole(@PathVariable Integer current, @PathVariable Integer size) {
        return R.ok().data(roleService.pageRole(current, size)).message("分页查询角色列表成功!");
    }

    @OperateLogger(description = "模糊搜索角色")
    @ApiOperation(value = "模糊搜索角色")
    @PostMapping("/searchRole/{current}/{size}")
    public R searchRole(@PathVariable Integer current, @PathVariable Integer size, @RequestBody(required = false) RoleSearchVo roleSearchVo) {
        if (ObjectUtils.isEmpty(roleSearchVo)) {
            return R.error().message("请输入查询条件!");
        }
        return R.ok().data(roleService.searchRole(current, size, roleSearchVo)).message("搜索成功!");
    }

    @Transactional
    @OperateLogger(description = "根据角色id删除角色")
    @ApiOperation(value = "根据角色id删除角色")
    @DeleteMapping("/deleteRoleById/{roleId}")
    public R deleteRoleById(@PathVariable Integer roleId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentUserId = ((User) authentication.getPrincipal()).getUserId();
        roleService.deleteRedisKey(currentUserId);
        return roleService.deleteRoleById(roleId) ? R.ok().message("删除角色成功!") : R.error().message("删除角色失败!");
    }

    @OperateLogger(description = "查询所有角色")
    @ApiOperation(value = "查询所有角色")
    @GetMapping("/listAllRole")
    public R listAllRole() {
        return R.ok().data("roleList", roleService.listAllRole()).message("查询所有角色成功!");
    }
}

