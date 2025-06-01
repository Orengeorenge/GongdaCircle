package com.gongda.gongdacircle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gongda.gongdacircle.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 帖子收藏实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("post_collect")
public class PostCollect extends BaseEntity {
    
    /**
     * 帖子ID
     */
    private Long postId;
    
    /**
     * 用户ID
     */
    private Long userId;
} 