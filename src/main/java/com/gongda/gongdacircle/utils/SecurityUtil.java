package com.gongda.gongdacircle.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全工具类
 * 用于获取当前登录用户信息
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
public class SecurityUtil {
    
    /**
     * 获取当前登录用户ID
     * 
     * @return 用户ID，如果未登录返回null
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }
        return null;
    }
    
    /**
     * 检查是否已登录
     * 
     * @return 是否已登录
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() 
               && !"anonymousUser".equals(authentication.getPrincipal());
    }
    
    /**
     * 获取当前认证对象
     * 
     * @return 认证对象
     */
    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
} 