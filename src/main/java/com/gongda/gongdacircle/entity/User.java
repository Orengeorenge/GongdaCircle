package com.gongda.gongdacircle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gongda.gongdacircle.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体类
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class User extends BaseEntity {
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 性别 0-女 1-男 2-保密
     */
    private Integer gender;
    
    /**
     * 生日
     */
    private String birthday;
    
    /**
     * 个人简介
     */
    private String biography;
    
    /**
     * 学校
     */
    private String school;
    
    /**
     * 专业
     */
    private String major;
    
    /**
     * 年级
     */
    private String grade;
    
    /**
     * 状态 0-禁用 1-正常
     */
    private Integer status;
    
    /**
     * 最后登录时间
     */
    private String lastLoginTime;
    
    /**
     * 最后登录IP
     */
    private String lastLoginIp;
} 