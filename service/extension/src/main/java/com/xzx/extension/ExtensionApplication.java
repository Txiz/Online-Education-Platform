package com.xzx.extension;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 扩展服务启动类
 * 作者: xzx
 * 创建时间: 2021-03-28-21-45
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
@MapperScan("com.xzx.extension.mapper")
@ComponentScan({"com.xzx"})
public class ExtensionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExtensionApplication.class, args);
    }
}
