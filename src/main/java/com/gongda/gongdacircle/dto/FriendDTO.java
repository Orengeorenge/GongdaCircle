package com.gongda.gongdacircle.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 好友申请DTO
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Data
public class FriendDTO {
    
    /**
     * 好友用户ID
     */
    @NotNull(message = "好友用户ID不能为空")
    private Long friendId;
    
    /**
     * 申请备注信息
     */
    @Size(max = 200, message = "备注信息不能超过200个字符")
    private String remark;
    
    /**
     * 申请理由
     */
    @Size(max = 500, message = "申请理由不能超过500个字符")
    private String reason;
} 