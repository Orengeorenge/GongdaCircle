package com.gongda.gongdacircle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gongda.gongdacircle.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 帖子点赞实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("post_like")
public class PostLike extends BaseEntity {
    
    /**
     * 帖子ID
     */
    private Long postId;
    
    /**
     * 用户ID
     */
    private Long userId;
} 