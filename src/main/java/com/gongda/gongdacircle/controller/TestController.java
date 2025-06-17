package com.gongda.gongdacircle.controller;

import com.gongda.gongdacircle.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/test")
public class TestController {
    
    /**
     * 简单的测试端点
     */
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello! 应用运行正常! 时间: " + LocalDateTime.now());
    }
    
    /**
     * 状态检查端点
     */
    @GetMapping("/status")
    public Result<Map<String, Object>> status() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now());
        status.put("message", "工大圈后端服务运行中");
        return Result.success(status);
    }
} 