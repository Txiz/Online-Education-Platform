package com.xzx.education.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.education.entity.People;
import com.xzx.education.entity.PeopleCourse;
import com.xzx.education.mapper.PeopleCourseMapper;
import com.xzx.education.mapper.PeopleMapper;
import com.xzx.education.service.PeopleCourseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
@Service
public class PeopleCourseServiceImpl extends ServiceImpl<PeopleCourseMapper, PeopleCourse> implements PeopleCourseService {

    @Resource
    private PeopleMapper peopleMapper;

    @Resource
    private PeopleCourseMapper peopleCourseMapper;

    /**
     * 根据课程id删除教师课程关系，people分成教师和学生两种
     *
     * @param courseId 课程id
     */
    @Override
    public void deleteTeacherCourseByCourseId(Integer courseId) {
        QueryWrapper<People> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("is_who", 1);
        List<People> teacherList = peopleMapper.selectList(wrapper1);
        for (People teacher : teacherList) {
            QueryWrapper<PeopleCourse> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("people_id", teacher.getPeopleId());
            wrapper2.eq("course_id", courseId);
            peopleCourseMapper.delete(wrapper2);
        }
    }

    /**
     * 保存课程教师列表
     *
     * @param courseId   课程id
     * @param teacherIds 教师id列表
     */
    @Override
    public void saveTeacherCourses(Integer courseId, List<Integer> teacherIds) {
        PeopleCourse peopleCourse = new PeopleCourse();
        peopleCourse.setCourseId(courseId);
        for (Integer teacherId : teacherIds) {
            peopleCourse.setPeopleId(teacherId);
            peopleCourseMapper.insert(peopleCourse);
        }
    }
}
