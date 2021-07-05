package com.xzx.admin.config.security;

import com.xzx.admin.dto.ApiDto;
import com.xzx.admin.entity.Role;
import com.xzx.admin.service.ApiService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 获取请求的url并进行验证
 * 作者: xzx
 * 创建时间: 2021-02-20-12-09
 **/
@Component
public class MyUrlFilter implements FilterInvocationSecurityMetadataSource {

    @Resource
    private ApiService apiService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        // 请求的url
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        List<ApiDto> apiList = apiService.getApisWithRole();
        for (ApiDto apiDto : apiList) {
            // 把所有api的访问路径和当前请求路径匹配，
            boolean match = antPathMatcher.match(apiDto.getApiUrl(), requestUrl);
            // 如果可以匹配
            if (match) {
                // 并且是需要授权的接口
                if (apiDto.getIsAuth()) {
                    // 就把apiDto下的角色列表传过来
                    String[] str = apiDto.getRoles().stream().map(Role::getRoleName).toArray(String[]::new);
                    if (str.length == 0)
                        return SecurityConfig.createList("ROLE_deny");
                    return SecurityConfig.createList(str);
                }
                // 如果不需要授权，给一个show角色
                else {
                    return SecurityConfig.createList("ROLE_show");
                }
            }
        }
        // 匹配不上，就给一个默认的登录角色
        return SecurityConfig.createList("ROLE_login");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
