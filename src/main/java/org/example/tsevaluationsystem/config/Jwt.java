package org.example.tsevaluationsystem.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.tsevaluationsystem.dto.UserInfo;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT配置类
 * <p>负责JWT Token的生成与解析</p>
 */
public class Jwt {

    // JWT 密钥（建议使用更复杂的密钥，可以从配置文件读取）
    private static final String SECRET = "your-256-bit-secret-your-256-bit-secret";
    
    // Token 有效期（毫秒）- 24小时
    private static final long EXPIRATION = 86400000;

    // 生成安全的密钥
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * 生成 JWT Token
     * @param userInfo 用户信息
     * @return JWT 字符串
     */
    public static String getJwt(UserInfo userInfo) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .subject(userInfo.getUsername())
                .claim("userId", userInfo.getUserId())
                .claim("status", userInfo.getStatus())
                .claim("infoId", userInfo.getInfoId())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(KEY)
                .compact();
    }

    /**
     * 解析 JWT Token
     * @param token JWT 字符串
     * @return Claims
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从 Token 中提取用户信息
     * @param token JWT 字符串
     * @return Map 包含 userId, status, infoId, username
     */
    public static Map<String, Object> getUserFromToken(String token) {
        Claims claims = parseToken(token);
        Map<String, Object> user = new HashMap<>();
        user.put("username", claims.getSubject());
        user.put("userId", claims.get("userId"));
        user.put("status", claims.get("status"));
        user.put("infoId", claims.get("infoId"));
        return user;
    }
}
