package com.xzx.education;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 在线教育平台启动器
 * 作者: xzx
 * 创建时间: 2021-03-05-11-26
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.xzx.education.mapper")
@ComponentScan({"com.xzx"})
public class EducationApplication {
    public static void main(String[] args) {
        SpringApplication.run(EducationApplication.class, args);
    }
}
