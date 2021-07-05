package com.xzx.admin.config.security;

import com.xzx.admin.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.xzx.admin.util.JwtCode.TOKEN_HEAD;
import static com.xzx.admin.util.JwtCode.TOKEN_HEADER;


/**
 * Jwt登录认证过滤器
 * 作者: xzx
 * 创建时间: 2021-02-11-15-00
 **/
public class JwtOncePerRequestFilter extends OncePerRequestFilter {

    @Resource
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(TOKEN_HEADER);
        // 存在token
        if (authHeader != null && authHeader.startsWith(TOKEN_HEAD)) {
            String token = authHeader.substring(TOKEN_HEAD.length());
            String username = JwtUtil.getUsernameFromToken(token);
            // 存在token，但是没有登录
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // 验证token是否有效
                if (JwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
