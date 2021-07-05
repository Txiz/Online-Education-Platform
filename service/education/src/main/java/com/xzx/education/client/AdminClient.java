package com.xzx.education.client;

import com.xzx.common.result.R;
import com.xzx.education.config.FeignConfig;
import com.xzx.education.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 服务转发，用户服务
 * 作者: xzx
 * 创建时间: 2021-03-19-13-34
 **/
@FeignClient(name = "admin-api", configuration = FeignConfig.class)
@Component
public interface AdminClient {

    @PostMapping("/admin/user/saveOrUpdateUser")
    R saveOrUpdateUser(@RequestBody UserVo userVo);

    @GetMapping("/admin/user/getCurrentUser")
    R getCurrentUser();
}
