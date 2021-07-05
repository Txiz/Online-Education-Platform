package com.xzx.education.client;


import com.xzx.common.result.R;
import com.xzx.education.config.FeignConfig;
import com.xzx.education.entity.ExceptionLog;
import com.xzx.education.entity.OperateLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 服务转发，登录日志服务
 * 作者: xzx
 * 创建时间: 2021-03-28-21-17
 **/
@FeignClient(name = "extension-api", configuration = FeignConfig.class)
@Component
public interface ExtensionClient {

    @PostMapping("/extension/operate-log/saveOperateLog")
    R saveOperateLog(@RequestBody OperateLog operateLog);

    @PostMapping("/extension/exception-log/saveExceptionLog")
    R saveExceptionLog(@RequestBody ExceptionLog exceptionLog);
}
