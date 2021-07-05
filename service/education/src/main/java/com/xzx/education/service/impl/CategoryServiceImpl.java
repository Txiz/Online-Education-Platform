package com.xzx.education.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.education.entity.Category;
import com.xzx.education.mapper.CategoryMapper;
import com.xzx.education.service.CategoryService;
import com.xzx.education.vo.CategorySearchVo;
import com.xzx.education.vo.CategoryTreeVo;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 删除相关缓存
     */
    @Override
    public void deleteRedisKey() {
        redisTemplate.delete("category_list");
        redisTemplate.delete("category_total");
        redisTemplate.delete("category_current");
        redisTemplate.delete("category_size");
        redisTemplate.delete("category_parent");
        redisTemplate.delete("category_children");
    }

    /**
     * 检查redis中key是否为空
     *
     * @param categoryList    菜单列表
     * @param categoryTotal   菜单总数
     * @param categoryCurrent 当前页
     * @param categorySize    每页大小
     * @return 是否存在有key不存在
     */
    private boolean checkRedisKey(List<CategoryTreeVo> categoryList, Integer categoryTotal, Integer categoryCurrent, Integer categorySize) {
        return ObjectUtils.isEmpty(categoryList) ||
                ObjectUtils.isEmpty(categoryTotal) ||
                ObjectUtils.isEmpty(categoryCurrent) ||
                ObjectUtils.isEmpty(categorySize);
    }

    /**
     * 分页查询分类列表
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    @Override
    public Map<String, Object> pageCategory(Integer current, Integer size) {
        List<CategoryTreeVo> categoryList = (List<CategoryTreeVo>) redisTemplate.opsForValue().get("category_list");
        Integer categoryTotal = (Integer) redisTemplate.opsForValue().get("category_total");
        Integer categoryCurrent = (Integer) redisTemplate.opsForValue().get("category_current");
        Integer categorySize = (Integer) redisTemplate.opsForValue().get("category_size");
        if (checkRedisKey(categoryList, categoryTotal, categoryCurrent, categorySize) || !current.equals(categoryCurrent) || !size.equals(categorySize)) {
            Page<Category> categoryPage = new Page<>(current, size);
            QueryWrapper<Category> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", 0);
            categoryMapper.selectPage(categoryPage, wrapper);
            categoryList = setTree(categoryPage.getRecords());
            categoryTotal = Math.toIntExact(categoryPage.getTotal());
            redisTemplate.opsForValue().set("category_list", categoryList);
            redisTemplate.opsForValue().set("category_total", categoryTotal);
            redisTemplate.opsForValue().set("category_current", categoryPage.getCurrent());
            redisTemplate.opsForValue().set("category_size", categoryPage.getSize());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", categoryTotal);
        map.put("categoryList", categoryList);
        return map;
    }

    /**
     * 模糊查询分类
     *
     * @param current          当前页
     * @param size             每页大小
     * @param categorySearchVo 分类查询条件视图
     * @return 结果集
     */
    @Override
    public Map<String, Object> searchCategory(Integer current, Integer size, CategorySearchVo categorySearchVo) {
        String categoryName = categorySearchVo.getCategoryName();
        String beginTime = categorySearchVo.getBeginTime();
        String endTime = categorySearchVo.getEndTime();
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(categoryName)) wrapper.eq("category_name", categoryName);
        if (!ObjectUtils.isEmpty(beginTime)) wrapper.ge("create_time", beginTime);
        if (!ObjectUtils.isEmpty(endTime)) wrapper.le("create_time", endTime);
        wrapper.eq("parent_id", 0);
        Page<Category> categoryPage = new Page<>(current, size);
        categoryMapper.selectPage(categoryPage, wrapper);
        List<CategoryTreeVo> categoryList = setTree(categoryPage.getRecords());
        Map<String, Object> map = new HashMap<>();
        map.put("total", categoryPage.getTotal());
        map.put("categoryList", categoryList);
        return map;
    }

    /**
     * 根据分类id删除分类
     *
     * @param categoryId 分类id
     * @return 是否删除成功
     */
    @Override
    public boolean deleteCategoryById(Integer categoryId) {
        return categoryMapper.deleteById(categoryId) > 0;
    }

    /**
     * 查询所有一级分类
     *
     * @return 分类列表
     */
    @Override
    public List<Category> listAllParentCategory() {
        List<Category> categoryList = (List<Category>) redisTemplate.opsForValue().get("category_parent");
        if (ObjectUtils.isEmpty(categoryList)) {
            QueryWrapper<Category> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", 0);
            categoryList = categoryMapper.selectList(wrapper);
            redisTemplate.opsForValue().set("category_parent", categoryList);
        }
        return categoryList;
    }

    /**
     * 查询所有二级分类
     *
     * @return 分类列表
     */
    @Override
    public List<Category> listAllChildrenCategory() {
        List<Category> categoryList = (List<Category>) redisTemplate.opsForValue().get("category_children");
        if (ObjectUtils.isEmpty(categoryList)) {
            QueryWrapper<Category> wrapper = new QueryWrapper<>();
            wrapper.ne("parent_id", 0);
            categoryList = categoryMapper.selectList(wrapper);
            redisTemplate.opsForValue().set("category_children", categoryList);
        }
        return categoryList;
    }

    /**
     * 根据父分类id查询所有的子分类
     *
     * @param parentId 父分类id
     * @return 分类列表
     */
    @Override
    public List<Category> listChildrenCategoryByParentId(Integer parentId) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId);
        return categoryMapper.selectList(wrapper);
    }

    /**
     * 递归构建分类树
     *
     * @param categories 一级分类
     * @return 分类树视图列表
     */
    private List<CategoryTreeVo> setTree(List<Category> categories) {
        List<Category> allCategory = categoryMapper.selectList(null);
        List<CategoryTreeVo> categoryTreeVos = new ArrayList<>();
        for (Category category : categories) {
            CategoryTreeVo categoryTreeVo = new CategoryTreeVo();
            BeanUtils.copyProperties(category, categoryTreeVo);
            List<CategoryTreeVo> children = setChildren(allCategory, categoryTreeVo.getCategoryId());
            categoryTreeVo.setChildren(children);
            categoryTreeVos.add(categoryTreeVo);
        }
        return categoryTreeVos;
    }

    /**
     * 设置孩子
     *
     * @param allCategory 分类列表
     * @param parentId    父分类id
     * @return 孩子
     */
    private List<CategoryTreeVo> setChildren(List<Category> allCategory, Integer parentId) {
        List<CategoryTreeVo> children = new ArrayList<>();
        for (Category category : allCategory) {
            if (category.getParentId().equals(parentId)) {
                CategoryTreeVo categoryTreeVo = new CategoryTreeVo();
                BeanUtils.copyProperties(category, categoryTreeVo);
                children.add(categoryTreeVo);
            }
        }
        return children;
    }
}
