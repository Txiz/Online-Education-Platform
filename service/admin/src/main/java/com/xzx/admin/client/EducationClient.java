package com.xzx.admin.client;

import com.xzx.admin.config.FeignConfig;
import com.xzx.admin.entity.People;
import com.xzx.common.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 作者: xzx
 * 创建时间: 2021-04-02-11-06
 **/
@FeignClient(name = "education-api", configuration = FeignConfig.class)
@Component
public interface EducationClient {

    @PostMapping("/education/people/saveOrUpdatePeople")
    R saveOrUpdatePeople(@RequestBody People people);
}
