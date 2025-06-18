package com.gongda.gongdacircle.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * JWT 工具类
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Slf4j
@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    private static JwtUtil instance;
    
    public JwtUtil() {
        instance = this;
    }
    
    /**
     * 生成 JWT Token
     * 
     * @param username 用户名
     * @return JWT Token
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * 为兼容旧方法保留，但内部使用用户名作为subject
     * 
     * @param userId 用户ID（已不再使用）
     * @param username 用户名
     * @return JWT Token
     */
    public String generateToken(Long userId, String username) {
        return generateToken(username);
    }
    
    /**
     * 从 Token 中获取用户名
     * 
     * @param token JWT Token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }
    
    /**
     * 从请求中获取用户名
     * 
     * @param request HTTP请求
     * @return 用户名
     */
    public static String getUsernameFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (StringUtils.hasText(token) && instance != null) {
            return instance.getUsernameFromToken(token);
        }
        throw new RuntimeException("无法获取用户信息");
    }
    
    /**
     * 从请求中获取Token
     * 
     * @param request HTTP请求
     * @return Token
     */
    private static String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    /**
     * 从 Token 中获取过期时间
     * 
     * @param token JWT Token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }
    
    /**
     * 验证 Token 是否有效
     * 
     * @param token JWT Token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT token validation failed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 检查 Token 是否过期
     * 
     * @param token JWT Token
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    /**
     * 从 Token 中解析 Claims
     * 
     * @param token JWT Token
     * @return Claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 获取签名密钥
     * 
     * @return 签名密钥
     */
    private SecretKey getSigningKey() {
        // 确保密钥长度足够
        if (secret.length() < 32) {
            log.warn("JWT密钥长度不足，建议在配置中提供至少32个字符的密钥");
            // 使用填充技术扩展密钥到足够长度
            StringBuilder paddedSecret = new StringBuilder(secret);
            while (paddedSecret.length() < 32) {
                paddedSecret.append(secret);
            }
            return Keys.hmacShaKeyFor(paddedSecret.substring(0, 32).getBytes());
        }
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    /**
     * 为保持向后兼容，从token获取用户ID的方法仍然保留
     * 但不推荐使用，将返回null
     */
    @Deprecated
    public Long getUserIdFromToken(String token) {
        log.warn("调用已废弃的getUserIdFromToken方法，请改用getUsernameFromToken");
        return null;
    }
    
    /**
     * 为保持向后兼容，从请求获取用户ID的方法仍然保留
     * 但不推荐使用，将抛出异常
     */
    @Deprecated
    public static Long getUserIdFromRequest(HttpServletRequest request) {
        log.error("调用已废弃的getUserIdFromRequest方法，请改用getUsernameFromRequest");
        throw new RuntimeException("此方法已废弃，请使用getUsernameFromRequest");
    }
}