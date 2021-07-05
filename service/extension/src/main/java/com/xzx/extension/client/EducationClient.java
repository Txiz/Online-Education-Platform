package com.xzx.extension.client;

import com.xzx.extension.config.FeignConfig;
import com.xzx.extension.vo.CourseCardVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 作者: xzx
 * 创建时间: 2021-04-10-16-21
 **/
@FeignClient(name = "education-api", configuration = FeignConfig.class)
@Component
public interface EducationClient {

    @GetMapping("/education/course/setCourseCardVo")
    List<CourseCardVo> setAllCourseCardVo();
}
