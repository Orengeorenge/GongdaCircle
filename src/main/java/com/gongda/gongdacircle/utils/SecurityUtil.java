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
     * 获取当前登录用户名
     * 
     * @return 用户名，如果未登录返回null
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }
    
    /**
     * 获取当前登录用户ID - 已废弃，保留用于兼容
     * 
     * @return 用户ID，如果未登录返回null
     * @deprecated 请使用getCurrentUsername()方法
     */
    @Deprecated
    public static Long getCurrentUserId() {
        // 不再返回用户ID，但保留方法用于兼容
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