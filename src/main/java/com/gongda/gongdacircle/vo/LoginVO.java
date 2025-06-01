package com.gongda.gongdacircle.vo;

import lombok.Data;

/**
 * 登录响应VO
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Data
public class LoginVO {
    
    /**
     * JWT Token
     */
    private String token;
    
    /**
     * Token 类型
     */
    private String tokenType = "Bearer";
    
    /**
     * 过期时间（毫秒）
     */
    private Long expiresIn;
    
    /**
     * 用户信息
     */
    private UserVO userInfo;
    
    public LoginVO(String token, Long expiresIn, UserVO userInfo) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.userInfo = userInfo;
    }
} 