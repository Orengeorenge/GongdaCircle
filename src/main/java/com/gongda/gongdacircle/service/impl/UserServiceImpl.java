package com.gongda.gongdacircle.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongda.gongdacircle.dto.UserDTO;
import com.gongda.gongdacircle.entity.User;
import com.gongda.gongdacircle.mapper.UserMapper;
import com.gongda.gongdacircle.service.UserService;
import com.gongda.gongdacircle.utils.JwtUtil;
import com.gongda.gongdacircle.vo.LoginVO;
import com.gongda.gongdacircle.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    @Override
    public boolean register(UserDTO userDTO) {
        // 检查用户名、邮箱、手机号是否已存在
        if (existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (StrUtil.isNotBlank(userDTO.getEmail()) && existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        if (StrUtil.isNotBlank(userDTO.getPhone()) && existsByPhone(userDTO.getPhone())) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 创建用户
        User user = new User();
        BeanUtil.copyProperties(userDTO, user);
        // 密码加密
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setStatus(1); // 默认启用
        
        return save(user);
    }
    
    @Override
    public LoginVO login(String username, String password) {
        // 根据用户名查询用户
        User user = baseMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new RuntimeException("用户已被禁用");
        }
        
        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now().toString());
        updateById(user);
        
        // 生成JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        
        // 构造用户信息
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        // 不返回密码
        userVO = sanitizeUserVO(userVO);
        
        return new LoginVO(token, jwtExpiration, userVO);
    }
    
    @Override
    public UserVO getUserById(Long id) {
        User user = getById(id);
        if (user == null) {
            return null;
        }
        
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return sanitizeUserVO(userVO);
    }
    
    @Override
    public boolean updateUser(Long id, UserDTO userDTO) {
        User user = getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 检查重复性（排除自己）
        if (!user.getUsername().equals(userDTO.getUsername()) && existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (StrUtil.isNotBlank(userDTO.getEmail()) && !userDTO.getEmail().equals(user.getEmail()) 
            && existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        if (StrUtil.isNotBlank(userDTO.getPhone()) && !userDTO.getPhone().equals(user.getPhone()) 
            && existsByPhone(userDTO.getPhone())) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 更新用户信息（不更新密码、ID等敏感字段）
        BeanUtil.copyProperties(userDTO, user, "id", "password", "createTime", "createBy");
        
        return updateById(user);
    }
    
    @Override
    public Page<UserVO> getUserList(Integer page, Integer size, String keyword) {
        Page<User> userPage = new Page<>(page, size);
        
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            queryWrapper.like(User::getUsername, keyword)
                       .or()
                       .like(User::getNickname, keyword)
                       .or()
                       .like(User::getEmail, keyword);
        }
        queryWrapper.orderByDesc(User::getCreateTime);
        
        Page<User> result = page(userPage, queryWrapper);
        
        // 转换为VO
        Page<UserVO> voPage = new Page<>();
        BeanUtil.copyProperties(result, voPage, "records");
        
        List<UserVO> userVOList = result.getRecords().stream()
                .map(user -> {
                    UserVO userVO = new UserVO();
                    BeanUtil.copyProperties(user, userVO);
                    return sanitizeUserVO(userVO);
                })
                .collect(Collectors.toList());
        
        voPage.setRecords(userVOList);
        return voPage;
    }
    
    @Override
    public boolean updateUserStatus(Long id, Integer status) {
        User user = getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        user.setStatus(status);
        return updateById(user);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return baseMapper.selectByUsername(username) != null;
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return baseMapper.selectByEmail(email) != null;
    }
    
    @Override
    public boolean existsByPhone(String phone) {
        return baseMapper.selectByPhone(phone) != null;
    }
    
    /**
     * 清理用户VO，移除敏感信息
     */
    private UserVO sanitizeUserVO(UserVO userVO) {
        // 这里可以移除一些敏感信息，比如手机号部分隐藏等
        return userVO;
    }
} 