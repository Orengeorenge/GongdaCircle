package com.gongda.gongdacircle.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论视图对象
 */
@Data
public class CommentVO {
    
    /**
     * 评论ID
     */
    private Long id;
    
    /**
     * 帖子ID
     */
    private Long postId;
    
    /**
     * 评论用户ID
     */
    private Long userId;
    
    /**
     * 评论用户信息
     */
    private UserVO user;
    
    /**
     * 父评论ID
     */
    private Long parentId;
    
    /**
     * 回复用户ID
     */
    private Long replyUserId;
    
    /**
     * 回复用户信息
     */
    private UserVO replyUser;
    
    /**
     * 评论内容
     */
    private String content;
    
    /**
     * 图片URL列表
     */
    private List<String> imageList;
    
    /**
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 当前用户是否已点赞
     */
    private Boolean isLiked;
    
    /**
     * 状态
     */
    private Integer status;
    
    /**
     * 子评论列表（回复）
     */
    private List<CommentVO> replies;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 