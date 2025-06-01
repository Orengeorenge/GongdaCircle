package com.gongda.gongdacircle.controller;

import com.gongda.gongdacircle.common.Result;
import com.gongda.gongdacircle.service.UserService;
import com.gongda.gongdacircle.utils.SecurityUtil;
import com.gongda.gongdacircle.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证相关控制器
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/me")
    public Result<UserVO> getCurrentUser() {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "未登录");
            }
            
            UserVO userVO = userService.getUserById(userId);
            return userVO != null ? Result.success(userVO) : Result.error("用户信息不存在");
        } catch (Exception e) {
            log.error("获取当前用户信息异常：", e);
            return Result.error("获取用户信息失败");
        }
    }
    
    /**
     * 测试认证是否生效
     */
    @GetMapping("/test")
    public Result<String> testAuth() {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.success("认证成功！当前用户ID: " + userId);
    }
} 