package com.gongda.gongdacircle.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gongda.gongdacircle.common.Result;
import com.gongda.gongdacircle.dto.LoginDTO;
import com.gongda.gongdacircle.dto.UserDTO;
import com.gongda.gongdacircle.service.UserService;
import com.gongda.gongdacircle.vo.LoginVO;
import com.gongda.gongdacircle.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Boolean> register(@Validated @RequestBody UserDTO userDTO) {
        try {
            boolean result = userService.register(userDTO);
            if (result) {
                return Result.success(true, "注册成功");
            } else {
                return Result.error("注册失败");
            }
        } catch (Exception e) {
            log.error("用户注册异常：", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody LoginDTO loginDTO) {
        try {
            LoginVO loginVO = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
            return Result.success("登录成功", loginVO);
        } catch (Exception e) {
            log.error("用户登录异常：", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable Long id) {
        try {
            UserVO userVO = userService.getUserById(id);
            if (userVO != null) {
                return Result.success(userVO);
            } else {
                return Result.error("用户不存在");
            }
        } catch (Exception e) {
            log.error("获取用户信息异常：", e);
            return Result.error("获取用户信息失败");
        }
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Result<Boolean> updateUser(@PathVariable Long id, @Validated @RequestBody UserDTO userDTO) {
        try {
            boolean result = userService.updateUser(id, userDTO);
            if (result) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新用户信息异常：", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 分页查询用户列表
     */
    @GetMapping("/list")
    public Result<Page<UserVO>> getUserList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        try {
            Page<UserVO> result = userService.getUserList(page, size, keyword);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询用户列表异常：", e);
            return Result.error("查询用户列表失败");
        }
    }
    
    /**
     * 更新用户状态
     */
    @PutMapping("/{id}/status")
    public Result<Boolean> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            boolean result = userService.updateUserStatus(id, status);
            if (result) {
                return Result.success("状态更新成功");
            } else {
                return Result.error("状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新用户状态异常：", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check/username")
    public Result<Boolean> checkUsername(@RequestParam String username) {
        try {
            boolean exists = userService.existsByUsername(username);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查用户名异常：", e);
            return Result.error("检查用户名失败");
        }
    }
    
    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check/email")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        try {
            boolean exists = userService.existsByEmail(email);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查邮箱异常：", e);
            return Result.error("检查邮箱失败");
        }
    }
    
    /**
     * 检查手机号是否存在
     */
    @GetMapping("/check/phone")
    public Result<Boolean> checkPhone(@RequestParam String phone) {
        try {
            boolean exists = userService.existsByPhone(phone);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查手机号异常：", e);
            return Result.error("检查手机号失败");
        }
    }
}