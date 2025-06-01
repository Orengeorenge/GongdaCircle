package com.gongda.gongdacircle.security;

import com.alibaba.fastjson.JSON;
import com.gongda.gongdacircle.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 认证入口点
 * 处理未认证的请求
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response, 
                        AuthenticationException authException) throws IOException {
        
        log.warn("Unauthorized access attempt from IP: {}, URI: {}", 
                request.getRemoteAddr(), request.getRequestURI());
        
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        Result<String> result = Result.error(401, "未授权访问，请先登录");
        response.getWriter().write(JSON.toJSONString(result));
    }
} 