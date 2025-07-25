package com.gongda.gongdacircle.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gongda.gongdacircle.common.Result;
import com.gongda.gongdacircle.dto.LoginDTO;
import com.gongda.gongdacircle.dto.PasswordChangeDTO;
import com.gongda.gongdacircle.dto.UserDTO;
import com.gongda.gongdacircle.dto.UserUpdateDTO;
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
    @GetMapping("/{username}")
    public Result<UserVO> getUserByUsername(@PathVariable String username) {
        try {
            UserVO userVO = userService.getUserByUsername(username);
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
     * 更新用户信息（旧接口，保留向后兼容性）
     */
    @PutMapping("/{username}")
    public Result<UserVO> updateUser(@PathVariable String username, @Validated @RequestBody UserDTO userDTO) {
        try {
            boolean result = userService.updateUserByUsername(username, userDTO);
            if (result) {
                // 获取并返回更新后的用户信息
                UserVO updatedUser = userService.getUserByUsername(username);
                if (updatedUser != null) {
                    return Result.success("更新成功", updatedUser);
                } else {
                    return Result.error("更新成功但获取最新信息失败");
                }
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新用户信息异常：", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新用户个人资料（新接口，不需要密码字段）
     */
    @PutMapping("/{username}/profile")
    public Result<UserVO> updateUserProfile(@PathVariable String username, @Validated @RequestBody UserUpdateDTO updateDTO) {
        try {
            boolean result = userService.updateUserProfileByUsername(username, updateDTO);
            if (result) {
                // 获取并返回更新后的用户信息
                UserVO updatedUser = userService.getUserByUsername(username);
                if (updatedUser != null) {
                    return Result.success("更新成功", updatedUser);
                } else {
                    return Result.error("更新成功但获取最新信息失败");
                }
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新用户个人资料异常：", e);
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
    @PutMapping("/{username}/status")
    public Result<Boolean> updateUserStatus(@PathVariable String username, @RequestParam Integer status) {
        try {
            boolean result = userService.updateUserStatusByUsername(username, status);
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
    
    /**
     * 修改密码
     */
    @PutMapping("/{username}/password")
    public Result<Boolean> changePassword(@PathVariable String username, @Validated @RequestBody PasswordChangeDTO passwordChangeDTO) {
        try {
            boolean result = userService.changePassword(username, passwordChangeDTO.getOldPassword(), passwordChangeDTO.getNewPassword());
            if (result) {
                return Result.success("密码修改成功");
            } else {
                return Result.error("密码修改失败");
            }
        } catch (Exception e) {
            log.error("修改密码异常：", e);
            return Result.error(e.getMessage());
        }
    }
}