package com.xzx.admin.aspect;

import com.xzx.admin.annotation.OperateLogger;
import com.xzx.admin.client.ExtensionClient;
import com.xzx.admin.entity.OperateLog;
import com.xzx.admin.entity.User;
import com.xzx.common.util.IpUtil;
import com.xzx.common.util.UserAgentUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 操作日志切面
 * 作者: xzx
 * 创建时间: 2021-03-28-22-32
 **/
@Component
@Aspect
public class OperateLogAspect {

    @Resource
    private ExtensionClient extensionClient;

    @Resource
    private UserAgentUtil userAgentUtil;

    @Pointcut("@annotation(operateLogger)")
    public void loginLogPointCut(OperateLogger operateLogger) {
    }

    @Around(value = "loginLogPointCut(operateLogger)", argNames = "joinPoint,operateLogger")
    public Object logAround(ProceedingJoinPoint joinPoint, OperateLogger operateLogger) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object res = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        int times = (int) (endTime - startTime);
        OperateLog operateLog = handleOperateLog(joinPoint, operateLogger, times);
        extensionClient.saveOperateLog(operateLog);
        return res;
    }

    private OperateLog handleOperateLog(ProceedingJoinPoint joinPoint, OperateLogger operateLogger, int times) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((User) authentication.getPrincipal()).getUsername();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String description = operateLogger.description();
        String ip = IpUtil.getIpAddress(request);
        String ipSource = IpUtil.getCityInfo(ip);
        Map<String, String> userAgent = userAgentUtil.parseOsAndBrowser(request.getHeader("User-Agent"));
        // 构建日志对象并返回
        OperateLog log = new OperateLog();
        log.setUsername(username);
        log.setUri(uri);
        log.setMethod(method);
        log.setDescription(description);
        log.setIp(ip);
        log.setIpSource(ipSource);
        log.setOs(userAgent.get("os"));
        log.setBrowser(userAgent.get("browser"));
        log.setTimes(times);
        log.setCreateTime(new Date());
        return log;
    }
}
