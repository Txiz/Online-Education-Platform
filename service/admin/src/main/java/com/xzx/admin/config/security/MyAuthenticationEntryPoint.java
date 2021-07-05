package com.xzx.admin.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzx.common.result.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 未登录或者token失效时自定义返回结果
 * 作者: xzx
 * 创建时间: 2021-02-11-15-08
 **/
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        R r = R.error().message("尚未登录!");
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(r));
        out.flush();
        out.close();
    }
}
