package com.gongda.gongdacircle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gongda.gongdacircle.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("comment")
public class Comment extends BaseEntity {
    
    private Long postId;
    private Long userId;
    private Long parentId;
    private Long replyUserId;
    private String content;
    private String images;
    private Integer likeCount;
    private Integer status;
} 