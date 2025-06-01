package com.gongda.gongdacircle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gongda.gongdacircle.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 好友关系实体
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("friend")
public class Friend extends BaseEntity {
    
    /**
     * 用户ID（申请者）
     */
    private Long userId;
    
    /**
     * 好友用户ID（被申请者）
     */
    private Long friendId;
    
    /**
     * 状态 0-待确认 1-已通过 2-已拒绝 3-已删除
     */
    private Integer status;
    
    /**
     * 备注信息
     */
    private String remark;
    
    /**
     * 申请理由
     */
    private String reason;
}