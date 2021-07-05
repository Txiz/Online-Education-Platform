package com.xzx.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xzx.admin.annotation.OperateLogger;
import com.xzx.admin.entity.User;
import com.xzx.admin.entity.UserRole;
import com.xzx.admin.service.UserRoleService;
import com.xzx.admin.service.UserService;
import com.xzx.admin.vo.PasswordVo;
import com.xzx.admin.vo.UserSearchVo;
import com.xzx.admin.vo.UserVo;
import com.xzx.common.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
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
@RequestMapping("/admin/user")
@Api(tags = "系统用户控制器")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Transactional
    @OperateLogger(description = "保存或者更新用户")
    @ApiOperation(value = "保存或者更新用户")
    @PostMapping("saveOrUpdateUser")
    public R saveOrUpdateUser(@RequestBody UserVo userVo) {
        if (userVo == null) return R.error().message("用户信息不能全部为空!");
        Integer userId = userVo.getUserId();
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        if (userId != null) {
            QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            userRoleService.remove(wrapper);
        } else {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", userVo.getUsername());
            if (userService.count(wrapper) > 0) return R.error().message("用户名已存在!");
            if (StringUtils.isEmpty(userVo.getPassword()))
                user.setPassword(passwordEncoder.encode("111111"));
            else
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            if (StringUtils.isEmpty(userVo.getAvatar()))
                user.setAvatar("https://xzx-oep.oss-cn-shanghai.aliyuncs.com/2021/03/29/c1c58de6e4e14bf7a32309fb8c16a647-头像2.png");
        }
        userService.saveOrUpdate(user);
        userRoleService.saveUserRoles(user.getUserId(), userVo.getRoleIds());
        userService.deleteRedisKey(userId);
        return R.ok().message("保存或者更新用户成功!");
    }

    @OperateLogger(description = "分页查询用户列表")
    @ApiOperation(value = "分页查询用户列表")
    @GetMapping("/pageUser/{current}/{size}")
    public R pageUser(@PathVariable Integer current, @PathVariable Integer size) {
        return R.ok().data(userService.pageUser(current, size)).message("分页查询用户列表成功!");
    }

    @OperateLogger(description = "模糊查询用户")
    @ApiOperation(value = "模糊查询用户")
    @PostMapping("/searchUser/{current}/{size}")
    public R searchUser(@PathVariable Integer current, @PathVariable Integer size, @RequestBody(required = false) UserSearchVo userSearchVo) {
        if (ObjectUtils.isEmpty(userSearchVo)) {
            return R.error().message("请输入查询条件!");
        }
        return R.ok().data(userService.searchUser(current, size, userSearchVo)).message("搜索成功!");
    }

    @Transactional
    @OperateLogger(description = "根据用户id删除用户")
    @ApiOperation(value = "根据用户id删除用户")
    @DeleteMapping("/deleteUserById/{userId}")
    public R deleteUserById(@PathVariable Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentUserId = ((User) authentication.getPrincipal()).getUserId();
        if (userId.equals(currentUserId)) {
            return R.error().message("不允许删除当前登录用户");
        }
        userService.deleteRedisKey(userId);
        return userService.deleteUserById(userId) ? R.ok().message("删除用户成功!") : R.error().message("删除用户失败!");
    }

    @Transactional
    @OperateLogger(description = "修改当前登录用户的密码")
    @ApiOperation(value = "修改当前登录用户的密码")
    @PostMapping("/updatePassword")
    public R updatePassword(@RequestBody PasswordVo passwordVo) {
        // 获取当前登录用户的用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = ((User) authentication.getPrincipal());
        userService.deleteRedisKey(currentUser.getUserId());
        return userService.updatePassword(passwordVo, currentUser) ? R.ok().message("密码修改成功！") : R.error().message("密码修改失败！");
    }

    @OperateLogger(description = "获取当前登录用户")
    @ApiOperation(value = "获取当前登录用户")
    @GetMapping("/getCurrentUser")
    public R getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = ((User) authentication.getPrincipal());
        return R.ok().data("currentUser", currentUser);
    }

    @OperateLogger(description = "修改当前用户信息")
    @ApiOperation(value = "修改当前用户信息")
    @PostMapping("/updateCurrentUser")
    public R updateCurrentUser(@RequestBody User currentUser) {
        User user = new User();
        user.setUserId(currentUser.getUserId());
        user.setUsername(currentUser.getUsername());
        user.setDescription(currentUser.getDescription());
        user.setAvatar(currentUser.getAvatar());
        userService.saveOrUpdate(user);
        userService.deleteRedisKey(user.getUserId());
        return R.ok().message("修改成功");
    }
}

