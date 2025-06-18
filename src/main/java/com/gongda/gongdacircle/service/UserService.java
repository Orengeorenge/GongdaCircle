package com.gongda.gongdacircle.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gongda.gongdacircle.dto.UserDTO;
import com.gongda.gongdacircle.dto.UserUpdateDTO;
import com.gongda.gongdacircle.entity.User;
import com.gongda.gongdacircle.vo.UserVO;
import com.gongda.gongdacircle.vo.LoginVO;

/**
 * 用户服务接口
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户注册
     * @param userDTO 用户信息
     * @return 注册结果
     */
    boolean register(UserDTO userDTO);
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录信息（包含JWT token和用户信息）
     */
    LoginVO login(String username, String password);
    
    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户视图对象
     */
    UserVO getUserById(Long id);
    
    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户视图对象
     */
    UserVO getUserByUsername(String username);
    
    /**
     * 更新用户信息
     * @param id 用户ID
     * @param userDTO 用户信息
     * @return 更新结果
     */
    boolean updateUser(Long id, UserDTO userDTO);
    
    /**
     * 根据用户名更新用户信息
     * @param username 用户名
     * @param userDTO 用户信息
     * @return 更新结果
     */
    boolean updateUserByUsername(String username, UserDTO userDTO);
    
    /**
     * 根据用户名更新用户信息（不需要密码）
     * @param username 用户名
     * @param updateDTO 用户更新信息
     * @return 更新结果
     */
    boolean updateUserProfileByUsername(String username, UserUpdateDTO updateDTO);
    
    /**
     * 修改用户密码
     * @param username 用户名
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    boolean changePassword(String username, String oldPassword, String newPassword);
    
    /**
     * 分页查询用户列表
     * @param page 页码
     * @param size 每页数量
     * @param keyword 搜索关键词
     * @return 用户分页数据
     */
    Page<UserVO> getUserList(Integer page, Integer size, String keyword);
    
    /**
     * 禁用/启用用户
     * @param id 用户ID
     * @param status 状态
     * @return 操作结果
     */
    boolean updateUserStatus(Long id, Integer status);
    
    /**
     * 根据用户名禁用/启用用户
     * @param username 用户名
     * @param status 状态
     * @return 操作结果
     */
    boolean updateUserStatusByUsername(String username, Integer status);
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 检查手机号是否存在
     * @param phone 手机号
     * @return 是否存在
     */
    boolean existsByPhone(String phone);
} 