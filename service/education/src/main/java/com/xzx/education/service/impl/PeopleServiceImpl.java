package com.xzx.education.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.education.client.AdminClient;
import com.xzx.education.entity.Course;
import com.xzx.education.entity.People;
import com.xzx.education.entity.PeopleCourse;
import com.xzx.education.listener.PeopleListener;
import com.xzx.education.mapper.CourseMapper;
import com.xzx.education.mapper.PeopleCourseMapper;
import com.xzx.education.mapper.PeopleMapper;
import com.xzx.education.service.CourseService;
import com.xzx.education.service.PeopleService;
import com.xzx.education.vo.CourseCardVo;
import com.xzx.education.vo.PeopleExcelVo;
import com.xzx.education.vo.PeopleSearchVo;
import com.xzx.education.vo.UserVo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
@Service
public class PeopleServiceImpl extends ServiceImpl<PeopleMapper, People> implements PeopleService {

    @Resource
    private PeopleMapper peopleMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private PeopleCourseMapper peopleCourseMapper;

    @Resource
    private CourseService courseService;

    @Resource
    private AdminClient adminClient;

    /**
     * 使用Excel批量导入账号
     *
     * @param file          Excel文件
     * @param peopleService 账号服务
     */
    @Override
    public void savePeopleByUseExcel(MultipartFile file, PeopleService peopleService) {
        try {
            InputStream in = file.getInputStream();
            EasyExcel.read(in, PeopleExcelVo.class, new PeopleListener(peopleService)).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * Excel中的单行数据视图
     *
     * @param peopleExcelVo 账号Excel表视图
     */
    @Override
    public void savePeopleByUseExcel(PeopleExcelVo peopleExcelVo) {
        QueryWrapper<People> wrapper = new QueryWrapper<>();
        wrapper.eq("people_name", peopleExcelVo.getPeopleName());
        if (peopleMapper.selectCount(wrapper) > 0)
            throw new RuntimeException("用户名称" + peopleExcelVo.getPeopleName() + "重复!");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        People people = new People();
        people.setPeopleName(peopleExcelVo.getPeopleName());
        people.setNickName(peopleExcelVo.getNickName());
        people.setPassword(passwordEncoder.encode("111111"));
        people.setIsWho(peopleExcelVo.getIsTeacherOrStudent().equals("教师"));
        people.setAvatar("https://xzx-oep.oss-cn-shanghai.aliyuncs.com/2021/03/29/c1c58de6e4e14bf7a32309fb8c16a647-头像2.png");
        peopleMapper.insert(people);
        UserVo userVo = new UserVo();
        userVo.setUsername(people.getPeopleName());
        userVo.setPassword("111111");
        userVo.setDescription(people.getNickName());
        userVo.setAvatar(people.getAvatar());
        if (people.getIsWho()) userVo.setRoleIds(Collections.singletonList(3));
        else userVo.setRoleIds(Collections.singletonList(4));
        adminClient.saveOrUpdateUser(userVo);
    }

    /**
     * 分页查询账号列表
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    @Override
    public Map<String, Object> pagePeople(Integer current, Integer size) {
        Page<People> peoplePage = new Page<>(current, size);
        peopleMapper.selectPage(peoplePage, null);
        Map<String, Object> map = new HashMap<>();
        map.put("total", peoplePage.getTotal());
        map.put("peopleList", peoplePage.getRecords());
        return map;
    }

    /**
     * 模糊查询账号
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    @Override
    public Map<String, Object> searchPeople(Integer current, Integer size, PeopleSearchVo peopleSearchVo) {
        String peopleName = peopleSearchVo.getPeopleName();
        String nickName = peopleSearchVo.getNickName();
        Boolean isWho = peopleSearchVo.getIsWho();
        String beginTime = peopleSearchVo.getBeginTime();
        String endTime = peopleSearchVo.getEndTime();
        QueryWrapper<People> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(peopleName)) wrapper.eq("people_name", peopleName);
        if (!ObjectUtils.isEmpty(nickName)) wrapper.eq("nick_name", nickName);
        if (!ObjectUtils.isEmpty(isWho)) wrapper.eq("is_who", isWho);
        if (!ObjectUtils.isEmpty(beginTime)) wrapper.ge("create_time", beginTime);
        if (!ObjectUtils.isEmpty(endTime)) wrapper.le("create_time", endTime);
        Page<People> peoplePage = new Page<>(current, size);
        peopleMapper.selectPage(peoplePage, wrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("total", peoplePage.getTotal());
        map.put("peopleList", peoplePage.getRecords());
        return map;
    }

    /**
     * 根据账号id删除账号
     *
     * @param peopleId 账号id
     * @return 是否删除成功!
     */
    @Override
    public boolean deletePeopleById(Integer peopleId) {
        return peopleMapper.deleteById(peopleId) > 0;
    }

    /**
     * 查询所有的教师账号
     *
     * @param current 当前页
     * @return 查询所有的教师账号
     */
    @Override
    public Map<String, Object> listAllTeacher(Integer current) {
        QueryWrapper<People> wrapper = new QueryWrapper<>();
        wrapper.eq("is_who", 1);
        Page<People> peoplePage = new Page<>(current, 8);
        peopleMapper.selectPage(peoplePage, wrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("teacherList", peoplePage.getRecords());
        map.put("current", peoplePage.getCurrent());
        map.put("total", peoplePage.getTotal());
        map.put("pages", peoplePage.getPages());
        map.put("hasPrevious", peoplePage.hasPrevious());
        map.put("hasNext", peoplePage.hasNext());
        return map;
    }

    @Override
    public List<People> listHotTeacher() {
        QueryWrapper<People> wrapper = new QueryWrapper<>();
        wrapper.eq("is_who", 1);
        Page<People> peoplePage = new Page<>(1, 8);
        peopleMapper.selectPage(peoplePage, wrapper);
        return peoplePage.getRecords();
    }

    /**
     * 前台方法——查询某个人
     *
     * @param peopleId 人id
     * @return 结果集
     */
    @Override
    public Map<String, Object> getByPeopleId(Integer peopleId) {
        People byId = getById(peopleId);
        List<Course> res = new ArrayList<>();
        for (Course course : courseMapper.selectList(null)) {
            QueryWrapper<PeopleCourse> wrapper = new QueryWrapper<>();
            wrapper.eq("course_id", course.getCourseId());
            wrapper.eq("people_id", peopleId);
            if (peopleCourseMapper.selectCount(wrapper) > 0) {
                res.add(course);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("teacher", byId);
        map.put("courseList", res);
        return map;
    }

    @Override
    public Map<String, Object> getByPeopleName(String peopleName) {
        QueryWrapper<People> wrapper = new QueryWrapper<>();
        wrapper.eq("people_name", peopleName);
        People one = getOne(wrapper);
        List<CourseCardVo> res = new ArrayList<>();
        for (Course course : courseMapper.selectList(null)) {
            QueryWrapper<PeopleCourse> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("course_id", course.getCourseId());
            wrapper2.eq("people_id", one.getPeopleId());
            if (peopleCourseMapper.selectCount(wrapper2) > 0) {
                CourseCardVo courseCardVo = courseService.setCourseCardVo(course);
                res.add(courseCardVo);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("teacher", one);
        map.put("courseCardVos", res);
        return map;
    }
}
