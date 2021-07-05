package com.xzx.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.admin.entity.Role;
import com.xzx.admin.entity.RoleApi;
import com.xzx.admin.entity.RoleMenu;
import com.xzx.admin.entity.UserRole;
import com.xzx.admin.mapper.RoleApiMapper;
import com.xzx.admin.mapper.RoleMapper;
import com.xzx.admin.mapper.RoleMenuMapper;
import com.xzx.admin.mapper.UserRoleMapper;
import com.xzx.admin.service.RoleService;
import com.xzx.admin.vo.RoleSearchVo;
import com.xzx.admin.vo.RoleTagVo;
import com.xzx.admin.vo.RoleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private RoleApiMapper roleApiMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 删除相关缓存
     *
     * @param userId 用户id
     */
    @Override
    public void deleteRedisKey(Integer userId) {
        redisTemplate.delete("role_list");
        redisTemplate.delete("role_total");
        redisTemplate.delete("role_current");
        redisTemplate.delete("role_size");
        redisTemplate.delete("role_tag_list");
        redisTemplate.delete("role_of_user_" + userId);
        redisTemplate.delete("menu_of_user_" + userId);
        redisTemplate.delete("user_list");
        redisTemplate.delete("api_dto_list");
    }

    /**
     * 检查redis中key是否为空
     *
     * @param roleVos     角色视图列表
     * @param roleTotal   角色总数
     * @param roleCurrent 当前页
     * @param roleSize    每页大小
     * @return 是否存在有key不存在
     */
    private boolean checkRedisKey(List<RoleVo> roleVos, Integer roleTotal, Integer roleCurrent, Integer roleSize) {
        return ObjectUtils.isEmpty(roleVos) ||
                ObjectUtils.isEmpty(roleTotal) ||
                ObjectUtils.isEmpty(roleCurrent) ||
                ObjectUtils.isEmpty(roleSize);
    }

    /**
     * 分页查询角色
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    @Override
    public Map<String, Object> pageRole(Integer current, Integer size) {
        List<RoleVo> roleVos = (List<RoleVo>) redisTemplate.opsForValue().get("role_list");
        Integer roleTotal = (Integer) redisTemplate.opsForValue().get("role_total");
        Integer roleCurrent = (Integer) redisTemplate.opsForValue().get("role_current");
        Integer roleSize = (Integer) redisTemplate.opsForValue().get("role_size");
        if (checkRedisKey(roleVos, roleTotal, roleCurrent, roleSize) || !current.equals(roleCurrent) || !size.equals(roleSize)) {
            Page<Role> rolePage = new Page<>(current, size);
            roleMapper.selectPage(rolePage, null);
            List<Role> roleList = rolePage.getRecords();
            roleVos = new ArrayList<>();
            for (Role role : roleList) {
                RoleVo roleVo = getRoleVoById(role);
                roleVos.add(roleVo);
            }
            roleTotal = Math.toIntExact(rolePage.getTotal());
            redisTemplate.opsForValue().set("role_list", roleVos);
            redisTemplate.opsForValue().set("role_total", roleTotal);
            redisTemplate.opsForValue().set("role_current", rolePage.getCurrent());
            redisTemplate.opsForValue().set("role_size", rolePage.getSize());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", roleTotal);
        map.put("roleList", roleVos);
        return map;
    }

    /**
     * 模糊查询角色
     *
     * @param current      当前页
     * @param size         每页大小
     * @param roleSearchVo 角色查询条件视图
     * @return 结果集
     */
    @Override
    public Map<String, Object> searchRole(Integer current, Integer size, RoleSearchVo roleSearchVo) {
        String roleName = roleSearchVo.getRoleName();
        String nickName = roleSearchVo.getNickName();
        String beginTime = roleSearchVo.getBeginTime();
        String endTime = roleSearchVo.getEndTime();
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(roleName)) wrapper.like("role_name", roleName);
        if (!ObjectUtils.isEmpty(nickName)) wrapper.like("nick_name", nickName);
        if (!ObjectUtils.isEmpty(beginTime)) wrapper.ge("create_time", beginTime);
        if (!ObjectUtils.isEmpty(endTime)) wrapper.le("create_time", endTime);
        Page<Role> rolePage = new Page<>(current, size);
        roleMapper.selectPage(rolePage, wrapper);
        List<Role> roleList = rolePage.getRecords();
        List<RoleVo> roleVos = new ArrayList<>();
        for (Role role : roleList) {
            RoleVo roleVo = getRoleVoById(role);
            roleVos.add(roleVo);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", rolePage.getTotal());
        map.put("roleList", roleVos);
        return map;
    }

    /**
     * 根据角色id删除角色
     *
     * @param roleId 角色id
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRoleById(Integer roleId) {
        QueryWrapper<RoleApi> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("role_id", roleId);
        roleApiMapper.delete(wrapper1);
        QueryWrapper<RoleMenu> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("role_id", roleId);
        roleMenuMapper.delete(wrapper2);
        QueryWrapper<UserRole> wrapper3 = new QueryWrapper<>();
        wrapper3.eq("role_id", roleId);
        userRoleMapper.delete(wrapper3);
        return roleMapper.deleteById(roleId) > 0;
    }

    /**
     * 查询所有角色
     *
     * @return 角色视图列表
     */
    @Override
    public List<RoleTagVo> listAllRole() {
        List<RoleTagVo> roleList = (List<RoleTagVo>) redisTemplate.opsForValue().get("role_tag_list");
        if (ObjectUtils.isEmpty(roleList)) {
            roleList = new ArrayList<>();
            List<Role> roles = roleMapper.selectList(null);
            for (Role role : roles) {
                RoleTagVo roleTagVo = new RoleTagVo();
                BeanUtils.copyProperties(role, roleTagVo);
                roleList.add(roleTagVo);
            }
            redisTemplate.opsForValue().set("role_tag_list", roleList);
        }
        return roleList;
    }

    /**
     * 根据用户id获取所有角色
     *
     * @param userId 用户id
     * @return 角色列表
     */
    @Override
    public List<Role> getRolesByUserId(Integer userId) {
        List<Role> roles = (List<Role>) redisTemplate.opsForValue().get("role_of_user_" + userId);
        if (ObjectUtils.isEmpty(roles)) {
            roles = roleMapper.getRolesByUserId(userId);
            redisTemplate.opsForValue().set("role_of_user_" + userId, roles);
        }
        return roles;
    }

    /**
     * 根据角色对象封装角色视图
     *
     * @param role 角色
     * @return 角色视图
     */
    private RoleVo getRoleVoById(Role role) {
        Integer roleId = role.getRoleId();
        RoleVo roleVo = new RoleVo();
        BeanUtils.copyProperties(role, roleVo);
        List<Integer> menuIds = new ArrayList<>();
        List<Integer> apiIds = new ArrayList<>();
        QueryWrapper<RoleMenu> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("role_id", roleId);
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(wrapper1);
        for (RoleMenu roleMenu : roleMenus) {
            menuIds.add(roleMenu.getMenuId());
        }
        QueryWrapper<RoleApi> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("role_id", roleId);
        List<RoleApi> roleApis = roleApiMapper.selectList(wrapper2);
        for (RoleApi roleApi : roleApis) {
            apiIds.add(roleApi.getApiId());
        }
        roleVo.setMenuIds(menuIds);
        roleVo.setApiIds(apiIds);
        return roleVo;
    }
}
