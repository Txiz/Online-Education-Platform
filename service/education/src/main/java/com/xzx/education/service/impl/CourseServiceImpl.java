package com.xzx.education.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.education.entity.*;
import com.xzx.education.mapper.*;
import com.xzx.education.service.CourseService;
import com.xzx.education.vo.CourseCardVo;
import com.xzx.education.vo.CourseSearchVo;
import com.xzx.education.vo.CourseVo;
import org.springframework.beans.BeanUtils;
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
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private PeopleMapper peopleMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private PeopleCourseMapper peopleCourseMapper;

    @Resource
    private CommentMapper commentMapper;

    /**
     * 分页查询课程
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    @Override
    public Map<String, Object> pageCourse(String peopleName, Integer current, Integer size) {
        Page<Course> coursePage = new Page<>(current, size);
        courseMapper.selectPage(coursePage, null);
        List<Course> courseList = coursePage.getRecords();
        List<Integer> courseIds = checkIsPeople(peopleName);
        List<CourseVo> courseVos = new ArrayList<>();
        for (Course course : courseList) {
            for (Integer courseId : courseIds) {
                if (course.getCourseId().equals(courseId)) {
                    CourseVo courseVo = getCourseVoById(course);
                    courseVos.add(courseVo);
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", coursePage.getTotal());
        map.put("courseList", courseVos);
        return map;
    }

    /**
     * 模糊查询课程
     *
     * @param current        当前页
     * @param size           每页大小
     * @param courseSearchVo 课程查询条件视图
     * @return 结果集
     */
    @Override
    public Map<String, Object> searchCourse(String peopleName, Integer current, Integer size, CourseSearchVo courseSearchVo) {
        String courseName = courseSearchVo.getCourseName();
        String description = courseSearchVo.getDescription();
        String beginTime = courseSearchVo.getBeginTime();
        String endTime = courseSearchVo.getEndTime();
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(courseName)) wrapper.like("course_name", courseName);
        if (!ObjectUtils.isEmpty(description)) wrapper.like("description", description);
        if (!ObjectUtils.isEmpty(beginTime)) wrapper.ge("create_time", beginTime);
        if (!ObjectUtils.isEmpty(endTime)) wrapper.le("create_time", endTime);
        Page<Course> coursePage = new Page<>(current, size);
        courseMapper.selectPage(coursePage, wrapper);
        List<Course> courseList = coursePage.getRecords();
        List<CourseVo> courseVos = new ArrayList<>();
        List<Integer> courseIds = checkIsPeople(peopleName);
        for (Course course : courseList) {
            for (Integer courseId : courseIds) {
                if (course.getCourseId().equals(courseId)) {
                    CourseVo courseVo = getCourseVoById(course);
                    courseVos.add(courseVo);
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", coursePage.getTotal());
        map.put("courseList", courseVos);
        return map;
    }

    /**
     * 根据课程id删除课程
     *
     * @param courseId 课程id
     * @return 是否删除成功
     */
    @Override
    public boolean deleteCourseById(Integer courseId) {
        QueryWrapper<PeopleCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        peopleCourseMapper.delete(wrapper);
        return courseMapper.deleteById(courseId) > 0;
    }

    /**
     * 定时任务调用——设置所有的课程卡片视图
     *
     * @return 统一结果封装
     */
    @Override
    public List<CourseCardVo> setAllCourseCardVo() {
        List<CourseCardVo> courseCardVos = new ArrayList<>();
        for (Course course : list()) {
            CourseCardVo courseCardVo = setCourseCardVo(course);
            courseCardVos.add(courseCardVo);
        }
        return courseCardVos;
    }

    /**
     * 根据课程对象封装课程视图
     *
     * @param course 课程
     * @return 课程视图
     */
    private CourseVo getCourseVoById(Course course) {
        Integer courseId = course.getCourseId();
        CourseVo courseVo = new CourseVo();
        BeanUtils.copyProperties(course, courseVo);
        List<Integer> teacherIds = new ArrayList<>();
        List<String> teacherNames = new ArrayList<>();
        QueryWrapper<PeopleCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        List<PeopleCourse> peopleCourses = peopleCourseMapper.selectList(wrapper);
        for (PeopleCourse peopleCourse : peopleCourses) {
            People people = peopleMapper.selectById(peopleCourse.getPeopleId());
            if (people.getIsWho().equals(true)) {
                teacherIds.add(people.getPeopleId());
                teacherNames.add(people.getNickName());
            }
        }
        Category category = categoryMapper.selectById(courseVo.getCategoryId());
        Category parentCategory = categoryMapper.selectById(category.getParentId());
        String categoryName = parentCategory.getCategoryName() + " -> " + category.getCategoryName();
        courseVo.setCategoryName(categoryName);
        courseVo.setTeacherIds(teacherIds);
        courseVo.setTeacherNames(teacherNames);
        return courseVo;
    }

    /**
     * 检查当前账号是否是User
     *
     * @param peopleName 账号名称
     * @return 课程id列表
     */
    private List<Integer> checkIsPeople(String peopleName) {
        List<Integer> courseIds;
        QueryWrapper<People> wrapper = new QueryWrapper<>();
        wrapper.eq("people_name", peopleName);
        if (peopleMapper.selectCount(wrapper) > 0)
            courseIds = courseMapper.listCourseIdsByPeopleName(peopleName);
        else
            courseIds = courseMapper.listCourseIds();
        return courseIds;
    }

    /**
     * 根据课程设置前台课程卡片视图
     *
     * @param course 课程
     * @return 课程卡片视图
     */
    @Override
    public CourseCardVo setCourseCardVo(Course course) {
        CourseCardVo courseCardVo = new CourseCardVo();
        BeanUtils.copyProperties(course, courseCardVo);
        List<String> teacherNames = new ArrayList<>();
        QueryWrapper<PeopleCourse> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("course_id", course.getCourseId());
        Integer studentNum = 0;
        for (PeopleCourse peopleCourse : peopleCourseMapper.selectList(wrapper1)) {
            People people = peopleMapper.selectById(peopleCourse.getPeopleId());
            if (people.getIsWho().equals(true)) teacherNames.add(people.getNickName());
            else studentNum++;
        }
        courseCardVo.setTeacherNames(teacherNames);
        courseCardVo.setStudentNum(studentNum);
        QueryWrapper<Comment> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("course_id", course.getCourseId());
        Integer commentNum = commentMapper.selectCount(wrapper2);
        courseCardVo.setCommentNum(commentNum);
        return courseCardVo;
    }

}
