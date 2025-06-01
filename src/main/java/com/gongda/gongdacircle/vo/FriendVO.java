package com.gongda.gongdacircle.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 好友关系VO
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Data
public class FriendVO {
    
    /**
     * 关系ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 好友用户ID
     */
    private Long friendId;
    
    /**
     * 好友用户信息
     */
    private UserVO friendInfo;
    
    /**
     * 状态 0-待确认 1-已通过 2-已拒绝 3-已删除
     */
    private Integer status;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
    /**
     * 备注信息
     */
    private String remark;
    
    /**
     * 申请理由
     */
    private String reason;
    
    /**
     * 是否为申请发起者
     */
    private Boolean isInitiator;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 