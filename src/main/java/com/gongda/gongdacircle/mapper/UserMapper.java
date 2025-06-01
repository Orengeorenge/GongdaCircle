package com.gongda.gongdacircle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongda.gongdacircle.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(String username);
    
    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户信息
     */
    User selectByEmail(String email);
    
    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户信息
     */
    User selectByPhone(String phone);
} 