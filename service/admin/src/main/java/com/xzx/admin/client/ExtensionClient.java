package com.xzx.admin.client;

import com.xzx.admin.config.FeignConfig;
import com.xzx.admin.entity.ExceptionLog;
import com.xzx.admin.entity.LoginLog;
import com.xzx.admin.entity.OperateLog;
import com.xzx.common.result.R;
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

    @PostMapping("/extension/login-log/saveLoginLog")
    R saveLoginLog(@RequestBody LoginLog loginLog);

    @PostMapping("/extension/operate-log/saveOperateLog")
    R saveOperateLog(@RequestBody OperateLog operateLog);

    @PostMapping("/extension/exception-log/saveExceptionLog")
    R saveExceptionLog(@RequestBody ExceptionLog exceptionLog);
}
