package com.xzx.education.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xzx.common.result.R;
import com.xzx.education.annotation.OperateLogger;
import com.xzx.education.client.AdminClient;
import com.xzx.education.entity.Comment;
import com.xzx.education.entity.People;
import com.xzx.education.entity.PeopleCourse;
import com.xzx.education.service.CommentService;
import com.xzx.education.service.PeopleCourseService;
import com.xzx.education.service.PeopleService;
import com.xzx.education.vo.PeopleCourseVo;
import com.xzx.education.vo.PeopleSearchVo;
import com.xzx.education.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Collections;
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
@RequestMapping("/education/people")
@Api(tags = "账号控制器")
public class PeopleController {

    @Resource
    private PeopleService peopleService;

    @Resource
    private PeopleCourseService peopleCourseService;

    @Resource
    private CommentService commentService;

    @Resource
    private AdminClient adminClient;

    @Transactional
    @OperateLogger(description = "使用Excel表批量导入账号")
    @ApiOperation(value = "使用Excel表批量导入账号")
    @PostMapping("/savePeopleByUseExcel")
    public R savePeopleByUseExcel(MultipartFile file) {
        peopleService.savePeopleByUseExcel(file, peopleService);
        return R.ok().message("批量导入账号结束!");
    }

    @Transactional
    @OperateLogger(description = "保存或者更新账号")
    @ApiOperation(value = "保存或者更新账号")
    @PostMapping("/saveOrUpdatePeople")
    public R saveOrUpdatePeople(@RequestBody People people) {
        if (people == null) return R.error().message("账号信息不能全部为空");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        QueryWrapper<People> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("people_name", people.getPeopleName());
        if (people.getPeopleId() == null && peopleService.count(wrapper2) > 0)
            return R.error().message("账号名称" + people.getPeopleName() + "重复!");
        String password = people.getPassword();
        if (StringUtils.isEmpty(password)) people.setPassword(passwordEncoder.encode("111111"));
        else people.setPassword(passwordEncoder.encode(password));
        if (StringUtils.isEmpty(people.getAvatar()))
            people.setAvatar("https://xzx-oep.oss-cn-shanghai.aliyuncs.com/2021/03/29/c1c58de6e4e14bf7a32309fb8c16a647-头像2.png");
        peopleService.saveOrUpdate(people);
        UserVo userVo = new UserVo();
        userVo.setUsername(people.getPeopleName());
        userVo.setPassword(password);
        userVo.setDescription(people.getNickName());
        userVo.setAvatar(people.getAvatar());
        if (people.getIsWho() != null && people.getIsWho()) userVo.setRoleIds(Collections.singletonList(3));
        else userVo.setRoleIds(Collections.singletonList(4));
        adminClient.saveOrUpdateUser(userVo);
        return R.ok().message("保存或更新账号成功!");
    }

    @OperateLogger(description = "分页查询账号列表")
    @ApiOperation(value = "分页查询账号列表")
    @GetMapping("/pagePeople/{current}/{size}")
    public R pagePeople(@PathVariable Integer current, @PathVariable Integer size) {
        return R.ok().data(peopleService.pagePeople(current, size)).message("分页查询账号列表成功!");
    }

    @OperateLogger(description = "模糊搜索账号")
    @ApiOperation(value = "模糊搜索账号")
    @PostMapping("/searchPeople/{current}/{size}")
    public R searchPeople(@PathVariable Integer current, @PathVariable Integer size, @RequestBody(required = false) PeopleSearchVo peopleSearchVo) {
        if (ObjectUtils.isEmpty(peopleSearchVo)) {
            return R.error().message("请输入查询条件!");
        }
        return R.ok().data(peopleService.searchPeople(current, size, peopleSearchVo)).message("搜索成功!");
    }

    @Transactional
    @OperateLogger(description = "根据id删除账号")
    @ApiOperation(value = "根据id删除账号")
    @DeleteMapping("/deletePeopleById/{peopleId}")
    public R deletePeopleById(@PathVariable Integer peopleId) {
        QueryWrapper<Comment> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("people_id", peopleId);
        List<Comment> comments = commentService.list(wrapper1);
        for (Comment comment : comments) {
            commentService.removeById(comment.getCommentId());
        }
        QueryWrapper<PeopleCourse> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("people_id", peopleId);
        peopleCourseService.remove(wrapper2);
        return peopleService.deletePeopleById(peopleId) ? R.ok().message("账号删除成功!") : R.error().message("账号删除失败!");
    }

    @OperateLogger(description = "获取当前登录账号名称")
    @ApiOperation(value = "获取当前登录账号名称")
    @GetMapping("/getCurrentPeople")
    public R getCurrentPeople() {
        return adminClient.getCurrentUser();
    }

    @ApiOperation(value = "查询所有的教师账号")
    @GetMapping("/listAllTeacher/{current}")
    public R listAllTeacher(@PathVariable Integer current) {
        return R.ok().data(peopleService.listAllTeacher(current)).message("查询所有的教师账号");
    }

    @ApiOperation(value = "前台方法——查询教师")
    @GetMapping("/listHotTeacher")
    public R listHotTeacher() {
        return R.ok().data("teacherList", peopleService.listHotTeacher()).message("查询最热门教师");
    }

    @ApiOperation(value = "前台方法——查询某个人")
    @GetMapping("/getByPeopleId/{peopleId}")
    public R getByPeopleId(@PathVariable Integer peopleId) {
        return R.ok().data(peopleService.getByPeopleId(peopleId)).message("查询最热门教师");
    }

    @ApiOperation(value = "前台方法——查询某个人")
    @GetMapping("/getByPeopleName/{peopleName}")
    public R getByPeopleName(@PathVariable String peopleName) {
        return R.ok().data(peopleService.getByPeopleName(peopleName)).message("查询最热门教师");
    }

    @ApiOperation(value = "前台方法——保存选课记录")
    @PostMapping("/savePeopleCourse")
    public R savePeopleCourse(@RequestBody PeopleCourseVo peopleCourseVo) {
        Integer courseId = peopleCourseVo.getCourseId();
        String peopleName = peopleCourseVo.getPeopleName();
        QueryWrapper<People> wrapper = new QueryWrapper<>();
        wrapper.eq("people_name", peopleName);
        People one = peopleService.getOne(wrapper);
        PeopleCourse peopleCourse = new PeopleCourse();
        peopleCourse.setCourseId(courseId);
        peopleCourse.setPeopleId(one.getPeopleId());
        peopleCourseService.save(peopleCourse);
        return R.ok();
    }

    @ApiOperation(value = "前台方法——保存选课记录")
    @DeleteMapping("/deletePeopleCourse")
    public R deletePeopleCourse(@RequestBody PeopleCourseVo peopleCourseVo) {
        Integer courseId = peopleCourseVo.getCourseId();
        String peopleName = peopleCourseVo.getPeopleName();
        QueryWrapper<People> wrapper = new QueryWrapper<>();
        wrapper.eq("people_name", peopleName);
        People one = peopleService.getOne(wrapper);
        QueryWrapper<PeopleCourse> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("course_id", courseId);
        wrapper1.eq("people_id", one.getPeopleId());
        peopleCourseService.remove(wrapper1);
        return R.ok();
    }
}

