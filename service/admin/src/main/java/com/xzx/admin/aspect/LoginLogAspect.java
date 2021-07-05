package com.xzx.admin.aspect;

import com.xzx.admin.annotation.LoginLogger;
import com.xzx.admin.client.ExtensionClient;
import com.xzx.admin.entity.LoginLog;
import com.xzx.admin.vo.UserLoginVo;
import com.xzx.common.util.IpUtil;
import com.xzx.common.util.UserAgentUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 登录日志切面
 * 作者: xzx
 * 创建时间: 2021-03-28-21-22
 **/
@Component
@Aspect
public class LoginLogAspect {

    @Resource
    private ExtensionClient extensionClient;

    // 解析浏览器信息
    @Resource
    private UserAgentUtil userAgentUtil;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(loginLogger)")
    public void loginLogPointCut(LoginLogger loginLogger) {
    }

    @Around(value = "loginLogPointCut(loginLogger)", argNames = "joinPoint,loginLogger")
    public Object logAround(ProceedingJoinPoint joinPoint, LoginLogger loginLogger) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object res = joinPoint.proceed();   //执行了login
        long endTime = System.currentTimeMillis();
        int times = (int) (endTime - startTime);
        LoginLog loginLog = handleLoginLog(joinPoint, loginLogger, times);
        extensionClient.saveLoginLog(loginLog);
        Integer loginNum = (Integer) redisTemplate.opsForValue().get("login_num");
        if (loginNum == null) loginNum = 0;
        redisTemplate.opsForValue().set("login_num", loginNum + 1);
        return res;
    }

    private LoginLog handleLoginLog(ProceedingJoinPoint joinPoint, LoginLogger loginLogger, int times) {
        Object[] args = joinPoint.getArgs();
        String username = ((UserLoginVo) args[0]).getUsername();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String ip = IpUtil.getIpAddress(request);
        String ipSource = IpUtil.getCityInfo(ip);
        Map<String, String> userAgent = userAgentUtil.parseOsAndBrowser(request.getHeader("User-Agent"));
        // 构建日志对象并返回
        LoginLog log = new LoginLog();
        log.setUsername(username);
        log.setIp(ip);
        log.setIpSource(ipSource);
        log.setOs(userAgent.get("os"));
        log.setBrowser(userAgent.get("browser"));
        log.setTimes(times);
        log.setCreateTime(new Date());
        return log;
    }
}
