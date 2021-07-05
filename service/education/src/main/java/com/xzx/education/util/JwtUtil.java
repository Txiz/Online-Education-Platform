package com.xzx.education.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import static com.xzx.education.util.JwtCode.SECRET;

/**
 * Jwt工具类
 * 作者: xzx
 * 创建时间: 2021-03-16-22-14
 **/
public class JwtUtil {
    /**
     * 根据token获取用户名
     *
     * @param token token
     * @return username
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
