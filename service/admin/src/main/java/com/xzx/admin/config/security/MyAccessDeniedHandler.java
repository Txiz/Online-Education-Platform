package com.xzx.admin.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzx.common.result.R;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 访问接口没有权限时自定义返回结果
 * 作者: xzx
 * 创建时间: 2021-02-11-15-12
 **/
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        R r = R.error().message("没有权限访问此接口，请联系管理员!");
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(r));
        out.flush();
        out.close();
    }
}
