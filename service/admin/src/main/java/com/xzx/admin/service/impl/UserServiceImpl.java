package com.xzx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.admin.entity.User;
import com.xzx.admin.entity.UserRole;
import com.xzx.admin.mapper.UserMapper;
import com.xzx.admin.mapper.UserRoleMapper;
import com.xzx.admin.service.UserService;
import com.xzx.admin.vo.PasswordVo;
import com.xzx.admin.vo.UserSearchVo;
import com.xzx.admin.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 删除所有的缓存
     *
     * @param userId 用户id
     */
    @Override
    public void deleteRedisKey(Integer userId) {
        redisTemplate.delete("user_list");
        redisTemplate.delete("user_total");
        redisTemplate.delete("user_current");
        redisTemplate.delete("user_size");
        redisTemplate.delete("role_of_user_" + userId);
    }

    /**
     * 检查redis中key是否为空
     *
     * @param userVos     用户视图列表
     * @param userTotal   用户总数
     * @param userCurrent 当前页
     * @param userSize    每页大小
     * @return 是否存在有key不存在
     */
    private boolean checkRedisKey(List<UserVo> userVos, Integer userTotal, Integer userCurrent, Integer userSize) {
        return ObjectUtils.isEmpty(userVos) ||
                ObjectUtils.isEmpty(userTotal) ||
                ObjectUtils.isEmpty(userCurrent) ||
                ObjectUtils.isEmpty(userSize);
    }

    /**
     * 分页查询用户
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    @Override
    public Map<String, Object> pageUser(Integer current, Integer size) {
        List<UserVo> userVos = (List<UserVo>) redisTemplate.opsForValue().get("user_list");
        Integer userTotal = (Integer) redisTemplate.opsForValue().get("user_total");
        Integer userCurrent = (Integer) redisTemplate.opsForValue().get("user_current");
        Integer userSize = (Integer) redisTemplate.opsForValue().get("user_size");
        if (checkRedisKey(userVos, userTotal, userCurrent, userSize) || !current.equals(userCurrent) || !size.equals(userSize)) {
            Page<User> userPage = new Page<>(current, size);
            userMapper.selectPage(userPage, null);
            List<User> userList = userPage.getRecords();
            userVos = new ArrayList<>();
            for (User user : userList) {
                UserVo userVo = getUserVoById(user);
                userVos.add(userVo);
            }
            userTotal = Math.toIntExact(userPage.getTotal());
            redisTemplate.opsForValue().set("user_list", userVos);
            redisTemplate.opsForValue().set("user_total", userTotal);
            redisTemplate.opsForValue().set("user_current", userPage.getCurrent());
            redisTemplate.opsForValue().set("user_size", userPage.getSize());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", userTotal);
        map.put("userList", userVos);
        return map;
    }

    /**
     * 模糊查询用户
     *
     * @param current      当前页
     * @param size         每页大小
     * @param userSearchVo 用户查询条件视图
     * @return 结果集
     */
    @Override
    public Map<String, Object> searchUser(Integer current, Integer size, UserSearchVo userSearchVo) {
        String username = userSearchVo.getUsername();
        String description = userSearchVo.getDescription();
        Boolean isEnable = userSearchVo.getIsEnable();
        String beginTime = userSearchVo.getBeginTime();
        String endTime = userSearchVo.getEndTime();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(username)) wrapper.like("username", username);
        if (!ObjectUtils.isEmpty(description)) wrapper.like("description", description);
        if (!ObjectUtils.isEmpty(isEnable)) wrapper.eq("is_enable", isEnable);
        if (!ObjectUtils.isEmpty(beginTime)) wrapper.ge("create_time", beginTime);
        if (!ObjectUtils.isEmpty(endTime)) wrapper.le("create_time", endTime);
        Page<User> userPage = new Page<>(current, size);
        userMapper.selectPage(userPage, wrapper);
        List<User> userList = userPage.getRecords();
        List<UserVo> userVos = new ArrayList<>();
        for (User user : userList) {
            UserVo userVo = getUserVoById(user);
            userVos.add(userVo);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", userPage.getTotal());
        map.put("userList", userVos);
        return map;
    }

    /**
     * 根据用户id删除用户
     *
     * @param userId 用户id
     * @return 是否删除成功
     */
    @Override
    public boolean deleteUserById(Integer userId) {
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        userRoleMapper.delete(wrapper);
        return userMapper.deleteById(userId) > 0;
    }

    /**
     * 更新当前登录用户密码
     *
     * @param passwordVo  密码视图
     * @param currentUser 当前登录用户
     * @return 是否修改成功
     */
    @Override
    public boolean updatePassword(PasswordVo passwordVo, User currentUser) {
        String oldPassword = passwordVo.getOp();
        String newPassword = passwordVo.getNp();
        String confirmPassword = passwordVo.getCp();
        if (!newPassword.equals(confirmPassword)) {
            return false;
        }
        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            return false;
        }
        User user = new User();
        user.setUserId(currentUser.getUserId());
        user.setPassword(passwordEncoder.encode(newPassword));
        return userMapper.updateById(user) > 0;
    }

    /**
     * 跟据用户名得到用户
     *
     * @param username 用户名
     * @return 用户对象
     */
    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return userMapper.selectOne(wrapper);
    }

    /**
     * 根据用户对象封装用户视图
     *
     * @param user 用户
     * @return 用户视图
     */
    private UserVo getUserVoById(User user) {
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        List<Integer> roleIds = new ArrayList<>();
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user.getUserId());
        List<UserRole> userRoles = userRoleMapper.selectList(wrapper);
        for (UserRole userRole : userRoles) {
            roleIds.add(userRole.getRoleId());
        }
        userVo.setRoleIds(roleIds);
        return userVo;
    }
}
