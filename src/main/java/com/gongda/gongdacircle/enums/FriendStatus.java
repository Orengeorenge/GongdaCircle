package com.gongda.gongdacircle.enums;

import lombok.Getter;

/**
 * 好友状态枚举
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Getter
public enum FriendStatus {
    
    /**
     * 待确认
     */
    PENDING(0, "待确认"),
    
    /**
     * 已通过
     */
    APPROVED(1, "已通过"),
    
    /**
     * 已拒绝
     */
    REJECTED(2, "已拒绝"),
    
    /**
     * 已删除
     */
    DELETED(3, "已删除");
    
    private final Integer code;
    private final String desc;
    
    FriendStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    /**
     * 根据code获取描述
     */
    public static String getDescByCode(Integer code) {
        for (FriendStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDesc();
            }
        }
        return "未知状态";
    }
} 