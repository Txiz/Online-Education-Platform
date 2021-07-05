package com.xzx.education.controller;


import com.xzx.common.result.R;
import com.xzx.education.annotation.OperateLogger;
import com.xzx.education.entity.Course;
import com.xzx.education.service.ChapterService;
import com.xzx.education.service.CommentService;
import com.xzx.education.service.CourseService;
import com.xzx.education.service.PeopleCourseService;
import com.xzx.education.vo.CourseCardVo;
import com.xzx.education.vo.CourseSearchVo;
import com.xzx.education.vo.CourseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
@RestController
@RequestMapping("/education/course")
@Api(tags = "课程控制器")
public class CourseController {

    @Resource
    private CourseService courseService;

    @Resource
    private ChapterService chapterService;

    @Resource
    private CommentService commentService;

    @Resource
    private PeopleCourseService peopleCourseService;

    @Transactional
    @OperateLogger(description = "保存或者更新课程")
    @ApiOperation(value = "保存或者更新课程")
    @PostMapping("/saveOrUpdateCourse")
    public R saveOrUpdateCourse(@RequestBody CourseVo courseVo) {
        if (courseVo == null) return R.error().message("课程信息不能全部为空");
        Integer courseId = courseVo.getCourseId();
        Course course = new Course();
        BeanUtils.copyProperties(courseVo, course);
        if (courseId != null) {
            peopleCourseService.deleteTeacherCourseByCourseId(courseId);
            course.setVersion(courseService.getById(courseId).getVersion());
        }
        courseService.saveOrUpdate(course);
        peopleCourseService.saveTeacherCourses(course.getCourseId(), courseVo.getTeacherIds());
        return R.ok().message("保存或更新课程成功!");
    }

    @OperateLogger(description = "分页查询课程列表")
    @ApiOperation(value = "分页查询课程列表")
    @GetMapping("/pageCourse/{peopleName}/{current}/{size}")
    public R pageCourse(@PathVariable String peopleName, @PathVariable Integer current, @PathVariable Integer size) {
        return R.ok().data(courseService.pageCourse(peopleName, current, size)).message("分页查询课程列表成功!");
    }

    @OperateLogger(description = "模糊搜索课程")
    @ApiOperation(value = "模糊搜索课程")
    @PostMapping("/searchCourse/{peopleName}/{current}/{size}")
    public R searchCourse(@PathVariable String peopleName, @PathVariable Integer current, @PathVariable Integer size, @RequestBody(required = false) CourseSearchVo courseSearchVo) {
        if (ObjectUtils.isEmpty(courseSearchVo)) {
            return R.error().message("请输入查询条件!");
        }
        return R.ok().data(courseService.searchCourse(peopleName, current, size, courseSearchVo)).message("搜索成功!");
    }

    @Transactional
    @OperateLogger(description = "根据课程id删除课程")
    @ApiOperation(value = "根据课程id删除课程")
    @DeleteMapping("/deleteCourseById/{courseId}")
    public R deleteCourseById(@PathVariable Integer courseId) {
        chapterService.deleteByCourseId(courseId);
        commentService.deleteByCourseId(courseId);
        return courseService.deleteCourseById(courseId) ? R.ok().message("删除课程成功!") : R.error().message("删除课程失败!");
    }

    @ApiOperation(value = "定时任务调用——设置所有的课程卡片视图")
    @GetMapping("/setCourseCardVo")
    public List<CourseCardVo> setAllCourseCardVo() {
        return courseService.setAllCourseCardVo();
    }

    @ApiOperation(value = "根据课程id查询课程详情")
    @GetMapping("/getByCourseId/{courseId}")
    public R getByCourseId(@PathVariable Integer courseId) {
        Course byId = courseService.getById(courseId);
        return R.ok().data("courseWebVo", byId).data("chapterVideoList", chapterService.listAllChapter(courseId));
    }
}

