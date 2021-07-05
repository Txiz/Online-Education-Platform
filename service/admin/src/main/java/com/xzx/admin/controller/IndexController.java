package com.xzx.admin.controller;

import com.xzx.admin.annotation.LoginLogger;
import com.xzx.admin.client.EducationClient;
import com.xzx.admin.entity.People;
import com.xzx.admin.entity.User;
import com.xzx.admin.service.IndexService;
import com.xzx.admin.service.UserService;
import com.xzx.admin.util.JwtUtil;
import com.xzx.admin.vo.PeopleRegisterVo;
import com.xzx.admin.vo.UserLoginVo;
import com.xzx.common.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static com.xzx.admin.util.JwtCode.TOKEN_HEAD;

/**
 * 系统入口控制器
 * 作者: xzx
 * 创建时间: 2021-03-19-09-39
 **/
@RestController
@RequestMapping("/admin/index")
@Api(tags = "系统入口控制器")
public class IndexController {

    @Resource
    private IndexService indexService;

    @Resource
    private UserService userService;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private EducationClient educationClient;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @LoginLogger(value = "登录")
    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public R login(@RequestBody UserLoginVo userLoginVo) {
        String username = userLoginVo.getUsername();
        User user = userService.getUserByUsername(username);
        if (user == null) return R.error().message("用户名不存在!");
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null || !passwordEncoder.matches(userLoginVo.getPassword(), userDetails.getPassword()))
            return R.error().message("密码不正确!");
        if (!userDetails.isEnabled()) return R.error().message("账号未启动，请联系管理员!");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        Integer userId = ((User) authenticationToken.getPrincipal()).getUserId();
        String token = JwtUtil.generateToken(userDetails);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("tokenHead", TOKEN_HEAD);
        indexService.deleteRedisKey(userId);
        return R.ok().data(map).message("登录成功!");
    }

    @ApiOperation(value = "注销登录")
    @PostMapping("/logout")
    public R logout() {
        return R.ok().message("注销成功!");
    }


    @Transactional
    @ApiOperation(value = "")
    @PostMapping("/register")
    public R register(@RequestBody PeopleRegisterVo peopleRegisterVo) {
        People people = new People();
        BeanUtils.copyProperties(peopleRegisterVo, people);
        Integer registerNum = (Integer) redisTemplate.opsForValue().get("register_num");
        if (registerNum == null) registerNum = 0;
        redisTemplate.opsForValue().set("register_num", registerNum + 1);
        return educationClient.saveOrUpdatePeople(people);
    }
}
