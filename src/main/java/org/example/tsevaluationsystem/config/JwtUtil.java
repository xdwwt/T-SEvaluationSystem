package org.example.tsevaluationsystem.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.util.Map;

public class JwtUtil {

    /**
     * 从请求头中获取 Token
     */
    public static String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 从请求中获取当前登录用户的 infoId
     */
    public static Long getCurrentInfoId(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null) {
            return null;
        }
        Map<String, Object> user = Jwt.getUserFromToken(token);
        Object infoId = user.get("infoId");
        return infoId != null ? Long.valueOf(infoId.toString()) : null;
    }

    /**
     * 从请求中获取当前登录用户的 userId
     */
    public static String getCurrentUserId(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null) {
            return null;
        }
        Map<String, Object> user = Jwt.getUserFromToken(token);
        return (String) user.get("userId");
    }

    /**
     * 从请求中获取当前登录用户的 status
     */
    public static Integer getCurrentStatus(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null) {
            return null;
        }
        Map<String, Object> user = Jwt.getUserFromToken(token);
        Object status = user.get("status");
        return status != null ? Integer.valueOf(status.toString()) : null;
    }
}
