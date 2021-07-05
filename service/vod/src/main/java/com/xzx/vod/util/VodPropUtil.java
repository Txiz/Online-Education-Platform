package com.xzx.vod.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Vod变量读入，从yml文件中读取
 * 作者: xzx
 * 创建时间: 2021-03-17-14-35
 **/
@Component
public class VodPropUtil implements InitializingBean {
    public static String KEY_ID;
    public static String KEY_SECRET;
    @Value("${aliyun.vod.file.keyid}")
    private String keyId;
    @Value("${aliyun.vod.file.keysecret}")
    private String keySecret;

    @Override
    public void afterPropertiesSet() throws Exception {
        KEY_ID = keyId;
        KEY_SECRET = keySecret;
    }
}
