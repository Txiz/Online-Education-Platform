package com.xzx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.admin.entity.Role;
import com.xzx.admin.vo.RoleSearchVo;
import com.xzx.admin.vo.RoleTagVo;

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
public interface RoleService extends IService<Role> {

    /**
     * 删除相关缓存
     *
     * @param UserId 用户id
     */
    void deleteRedisKey(Integer UserId);

    /**
     * 分页查询角色
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    Map<String, Object> pageRole(Integer current, Integer size);

    /**
     * 模糊查询角色
     *
     * @param current      当前页
     * @param size         每页大小
     * @param roleSearchVo 角色查询条件视图
     * @return 结果集
     */
    Map<String, Object> searchRole(Integer current, Integer size, RoleSearchVo roleSearchVo);

    /**
     * 根据角色id删除角色
     *
     * @param roleId 角色id
     * @return 是否删除成功
     */
    boolean deleteRoleById(Integer roleId);

    /**
     * 查询所有角色
     *
     * @return 角色视图列表
     */
    List<RoleTagVo> listAllRole();

    /**
     * 根据用户id获取所有的角色
     *
     * @param userId 用户id
     * @return 角色列表
     */
    List<Role> getRolesByUserId(Integer userId);
}
