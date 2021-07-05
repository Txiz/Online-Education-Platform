package com.xzx.education.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.education.entity.Category;
import com.xzx.education.vo.CategorySearchVo;

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
public interface CategoryService extends IService<Category> {

    /**
     * 删除相关缓存
     */
    void deleteRedisKey();

    /**
     * 分页查询分类列表
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    Map<String, Object> pageCategory(Integer current, Integer size);

    /**
     * 模糊查询分类
     *
     * @param current          当前页
     * @param size             每页大小
     * @param categorySearchVo 分类查询条件视图
     * @return 结果集
     */
    Map<String, Object> searchCategory(Integer current, Integer size, CategorySearchVo categorySearchVo);

    /**
     * 根据分类id删除分类
     *
     * @param categoryId 分类id
     * @return 是否删除成功
     */
    boolean deleteCategoryById(Integer categoryId);

    /**
     * 查询所有一级分类
     *
     * @return 分类列表
     */
    List<Category> listAllParentCategory();

    /**
     * 查询所有二级分类
     *
     * @return 分类列表
     */
    List<Category> listAllChildrenCategory();

    /**
     * 根据父分类id查询所有的子分类
     *
     * @param parentId 父分类id
     * @return 分类列表
     */
    List<Category> listChildrenCategoryByParentId(Integer parentId);
}
