package com.xzx.education.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.education.entity.Course;
import com.xzx.education.vo.CourseCardVo;
import com.xzx.education.vo.CourseSearchVo;

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
public interface CourseService extends IService<Course> {

    /**
     * 分页查询课程
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    Map<String, Object> pageCourse(String peopleName, Integer current, Integer size);

    /**
     * 模糊查询课程
     *
     * @param current        当前页
     * @param size           每页大小
     * @param courseSearchVo 课程查询条件视图
     * @return 结果集
     */
    Map<String, Object> searchCourse(String peopleName, Integer current, Integer size, CourseSearchVo courseSearchVo);

    /**
     * 根据课程id删除课程
     *
     * @param courseId 课程id
     * @return 是否删除成功
     */
    boolean deleteCourseById(Integer courseId);

    /**
     * 定时任务调用——设置所有的课程卡片视图
     *
     * @return 统一结果封装
     */
    List<CourseCardVo> setAllCourseCardVo();

    CourseCardVo setCourseCardVo(Course course);
}
