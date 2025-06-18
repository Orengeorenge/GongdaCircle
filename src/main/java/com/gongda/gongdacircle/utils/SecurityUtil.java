package com.gongda.gongdacircle.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import com.gongda.gongdacircle.entity.User;
import com.gongda.gongdacircle.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

/**
 * 安全工具类
 * 用于获取当前登录用户信息
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Slf4j
@Component
public class SecurityUtil {
    
    private static UserMapper userMapperStatic;
    
    @Autowired
    private UserMapper userMapper;
    
    @PostConstruct
    public void init() {
        SecurityUtil.userMapperStatic = userMapper;
    }
    
    /**
     * 获取当前登录用户名
     * 
     * @return 用户名，如果未登录返回null
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();
        }
        return null;
    }
    
    /**
     * 获取当前登录用户ID
     * 
     * @return 用户ID，如果未登录返回null
     */
    public static Long getCurrentUserId() {
        String username = getCurrentUsername();
        if (username == null) {
            log.warn("获取当前用户ID失败：用户未登录");
            return null;
        }
        
        try {
            if (userMapperStatic == null) {
                log.error("获取当前用户ID失败：UserMapper未注入");
                return null;
            }
            
            User user = userMapperStatic.selectByUsername(username);
            if (user == null) {
                log.warn("获取当前用户ID失败：用户 {} 不存在", username);
                return null;
            }
            
            return user.getId();
        } catch (Exception e) {
            log.error("获取当前用户ID异常", e);
            return null;
        }
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