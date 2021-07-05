package com.xzx.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzx.admin.entity.Menu;
import com.xzx.admin.vo.MenuSideVo;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户id查询菜单栏
     *
     * @param userId 用户id
     * @return 菜单栏视图列表
     */
    List<MenuSideVo> getMenusByUserId(Integer userId);
}
