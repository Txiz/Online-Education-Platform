package com.xzx.admin.aspect;

import com.xzx.admin.annotation.LoginLogger;
import com.xzx.admin.annotation.OperateLogger;
import com.xzx.admin.client.ExtensionClient;
import com.xzx.admin.entity.ExceptionLog;
import com.xzx.admin.entity.User;
import com.xzx.common.util.ErrorUtil;
import com.xzx.common.util.IpUtil;
import com.xzx.common.util.UserAgentUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * 异常日志切面
 * 作者: xzx
 * 创建时间: 2021-03-28-23-28
 **/
@Component
@Aspect
public class ExceptionLogAspect {

    @Resource
    private ExtensionClient extensionClient;

    @Resource
    private UserAgentUtil userAgentUtil;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Pointcut("execution(* com.xzx.admin.controller..*.*(..))")
    public void exceptionLogPointCut() {
    }

    @AfterThrowing(value = "exceptionLogPointCut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Exception e) {
        ExceptionLog exceptionLog = handleExceptionLog(joinPoint, e);
        extensionClient.saveExceptionLog(exceptionLog);
        Integer exceptionNum = (Integer) redisTemplate.opsForValue().get("exception_num");
        if (exceptionNum == null) exceptionNum = 0;
        redisTemplate.opsForValue().set("exception_num", exceptionNum + 1);
    }

    private ExceptionLog handleExceptionLog(JoinPoint joinPoint, Exception e) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((User) authentication.getPrincipal()).getUsername();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String description = getDescriptionFromAnnotations(joinPoint);
        String error = ErrorUtil.getStackTrace(e);
        String ip = IpUtil.getIpAddress(request);
        String ipSource = IpUtil.getCityInfo(ip);
        Map<String, String> userAgent = userAgentUtil.parseOsAndBrowser(request.getHeader("User-Agent"));
        // 构建日志对象并返回
        ExceptionLog log = new ExceptionLog();
        log.setUsername(username);
        log.setUri(uri);
        log.setMethod(method);
        log.setDescription(description);
        log.setError(error);
        log.setIp(ip);
        log.setIpSource(ipSource);
        log.setOs(userAgent.get("os"));
        log.setBrowser(userAgent.get("browser"));
        log.setCreateTime(new Date());
        return log;
    }

    private String getDescriptionFromAnnotations(JoinPoint joinPoint) {
        String description = "";
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        OperateLogger operateLogger = method.getAnnotation(OperateLogger.class);
        if (operateLogger != null) {
            description = operateLogger.description();
            return description;
        }
        LoginLogger loginLogger = method.getAnnotation(LoginLogger.class);
        if (loginLogger != null) {
            description = loginLogger.value();
            return description;
        }
        return description;
    }
}
