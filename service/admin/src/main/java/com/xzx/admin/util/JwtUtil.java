package com.xzx.admin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.xzx.admin.util.JwtCode.*;

/**
 * Jwt工具类
 * 作者: xzx
 * 创建时间: 2021-03-16-22-14
 **/
public class JwtUtil {
    /**
     * 根据用户信息生成token
     *
     * @param userDetails 用户信息
     * @return token
     */
    public static String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATE, new Date());
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

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

    /**
     * 判断token是否失效
     *
     * @param token       token
     * @param userDetails 用户信息
     * @return 是否有效
     */
    public static boolean validateToken(String token, UserDetails userDetails) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String username = claims.getSubject();
        Date expire = claims.getExpiration();
        return username.equals(userDetails.getUsername()) && !expire.before(new Date());
    }

    /**
     * 刷新token
     *
     * @param token token
     * @return 刷新后的token
     */
    public static String refreshToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        claims.put(CLAIM_KEY_CREATE, new Date());
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }
}
