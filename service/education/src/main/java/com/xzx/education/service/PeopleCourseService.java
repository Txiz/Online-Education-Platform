package com.xzx.education.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.education.entity.PeopleCourse;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
public interface PeopleCourseService extends IService<PeopleCourse> {

    /**
     * 根据课程id删除教师课程关系，people分成教师和学生两种
     *
     * @param courseId 课程id
     */
    void deleteTeacherCourseByCourseId(Integer courseId);

    /**
     * 保存课程教师列表
     *
     * @param courseId   课程id
     * @param teacherIds 教师id列表
     */
    void saveTeacherCourses(Integer courseId, List<Integer> teacherIds);
}
