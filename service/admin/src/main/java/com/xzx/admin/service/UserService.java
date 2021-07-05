package com.xzx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.admin.entity.User;
import com.xzx.admin.vo.PasswordVo;
import com.xzx.admin.vo.UserSearchVo;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
public interface UserService extends IService<User> {

    /**
     * 删除相关缓存
     *
     * @param userId 用户id
     */
    void deleteRedisKey(Integer userId);

    /**
     * 分页查询用户
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    Map<String, Object> pageUser(Integer current, Integer size);

    /**
     * 模糊查询用户
     *
     * @param current      当前页
     * @param size         每页大小
     * @param userSearchVo 用户查询条件视图
     * @return 结果集
     */
    Map<String, Object> searchUser(Integer current, Integer size, UserSearchVo userSearchVo);

    /**
     * 根据用户id删除用户
     *
     * @param userId 用户id
     * @return 是否删除
     */
    boolean deleteUserById(Integer userId);

    /**
     * 更新当前登录用户密码
     *
     * @param passwordVo  密码视图
     * @param currentUser 当前登录用户
     * @return 是否修改成功
     */
    boolean updatePassword(PasswordVo passwordVo, User currentUser);

    /**
     * 跟据用户名得到用户
     *
     * @param username 用户名
     * @return 用户对象
     */
    User getUserByUsername(String username);
}
