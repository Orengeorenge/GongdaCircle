package com.gongda.gongdacircle.service;

/**
 * 帖子点赞服务接口
 */
public interface PostLikeService {
    
    /**
     * 点赞帖子
     */
    Boolean likePost(Long postId, Long userId);
    
    /**
     * 取消点赞帖子
     */
    Boolean unlikePost(Long postId, Long userId);
    
    /**
     * 检查用户是否已点赞帖子
     */
    Boolean isUserLiked(Long postId, Long userId);
    
    /**
     * 获取帖子点赞数
     */
    Integer getLikeCount(Long postId);
} 