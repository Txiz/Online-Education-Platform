package com.xzx.education.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xzx.common.result.R;
import com.xzx.education.annotation.OperateLogger;
import com.xzx.education.entity.Category;
import com.xzx.education.entity.Course;
import com.xzx.education.service.CategoryService;
import com.xzx.education.service.CourseService;
import com.xzx.education.vo.CategorySearchVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/education/category")
@Api(tags = "分类控制器")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private CourseService courseService;

    @Transactional
    @OperateLogger(description = "保存或者更新分类")
    @ApiOperation(value = "保存或者更新分类")
    @PostMapping("/saveOrUpdateCategory")
    public R saveOrUpdateCategory(@RequestBody Category category) {
        if (category == null)
            return R.error().message("分类信息不能全部为空");
        categoryService.saveOrUpdate(category);
        categoryService.deleteRedisKey();
        return R.ok().message("保存或更新分类成功!");
    }

    @OperateLogger(description = "分页查询分类列表")
    @ApiOperation(value = "分页查询分类列表")
    @GetMapping("/pageCategory/{current}/{size}")
    public R pageCategory(@PathVariable Integer current, @PathVariable Integer size) {
        return R.ok().data(categoryService.pageCategory(current, size)).message("分页查询分类列表成功!");
    }

    @OperateLogger(description = "模糊搜索分类")
    @ApiOperation(value = "模糊搜索分类")
    @PostMapping("/searchCategory/{current}/{size}")
    public R searchCategory(@PathVariable Integer current, @PathVariable Integer size, @RequestBody(required = false) CategorySearchVo categorySearchVo) {
        if (ObjectUtils.isEmpty(categorySearchVo)) {
            return R.error().message("请输入查询条件!");
        }
        return R.ok().data(categoryService.searchCategory(current, size, categorySearchVo)).message("搜索成功!");
    }

    @Transactional
    @OperateLogger(description = "根据id删除分类")
    @ApiOperation(value = "根据id删除分类")
    @DeleteMapping("/deleteCategoryById/{categoryId}")
    public R deleteCategoryById(@PathVariable Integer categoryId) {
        Integer parentId = categoryService.getById(categoryId).getParentId();
        QueryWrapper<Category> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("parent_id", categoryId);
        if (parentId.equals(0) && categoryService.count(wrapper1) > 0) return R.error().message("该父分类下有子分类，无法删除!");
        QueryWrapper<Course> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("category_id", categoryId);
        if (courseService.count(wrapper2) > 0) return R.error().message("该分类下有课程，无法删除!");
        categoryService.deleteRedisKey();
        return categoryService.deleteCategoryById(categoryId) ? R.ok().message("分类删除成功!") : R.error().message("分类删除失败!");
    }

//    @OperateLogger(description = "查询所有父分类")
    @ApiOperation(value = "查询所有父分类")
    @GetMapping("/listAllParentCategory")
    public R listAllParentCategory() {
        return R.ok().data("parentCategory", categoryService.listAllParentCategory()).message("查询所有父分类成功!");
    }

    //    @OperateLogger(description = "根据父分类id查询所有的子分类")
    @ApiOperation(value = "根据父分类id查询所有的子分类")
    @GetMapping("/listChildrenCategoryByParentId/{parentId}")
    public R listChildrenCategoryByParentId(@PathVariable Integer parentId) {
        return R.ok().data("childrenCategory", categoryService.listChildrenCategoryByParentId(parentId)).message("根据父分类id查询所有的子分类");
    }

    @OperateLogger(description = "查询所有的子分类用于课程选择")
    @ApiOperation(value = "查询所有的子分类用于课程选择")
    @GetMapping("/listAllChildrenCategory")
    public R listAllChildrenCategory() {
        return R.ok().data("childrenCategory", categoryService.listAllChildrenCategory()).message("查询所有子分类成功!");
    }
}

