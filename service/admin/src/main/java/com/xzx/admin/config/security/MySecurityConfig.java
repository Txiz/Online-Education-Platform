package com.xzx.admin.config.security;

import com.xzx.admin.entity.User;
import com.xzx.admin.service.RoleService;
import com.xzx.admin.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * 安全框架配置
 * 作者: xzx
 * 创建时间: 2021-02-11-14-52
 **/
@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    @Resource
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Resource
    private MyUrlFilter myUrlFilter;

    @Resource
    private MyAccessDecisionManager myAccessDecisionManager;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭csrf
        http.csrf().disable();
        // 基于token，不需要session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 所有请求都要求授权
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .withObjectPostProcessor(
                        new ObjectPostProcessor<FilterSecurityInterceptor>() {
                            @Override
                            public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                                o.setSecurityMetadataSource(myUrlFilter);
                                o.setAccessDecisionManager(myAccessDecisionManager);
                                return o;
                            }
                        }
                );
        // 关闭缓存
        http.headers().cacheControl();
        // 添加jwt 登录授权过滤器
        http.addFilterBefore(jwtOncePerRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        // 添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                .accessDeniedHandler(myAccessDeniedHandler);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/admin/index/**",
                "/css/**",
                "/js/**",
                "/index.html",
                "favicon.ico",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/webjars/**",
                "/v2/api-docs/**"
        );
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userService.getUserByUsername(username);
            if (user != null) {
                user.setRoles(roleService.getRolesByUserId(user.getUserId()));
                return user;
            }
            throw new UsernameNotFoundException("用户名或密码不正确");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtOncePerRequestFilter jwtOncePerRequestFilter() {
        return new JwtOncePerRequestFilter();
    }
}
