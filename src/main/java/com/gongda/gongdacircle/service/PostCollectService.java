package com.gongda.gongdacircle.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gongda.gongdacircle.vo.PostVO;

/**
 * 帖子收藏服务接口
 */
public interface PostCollectService {
    
    /**
     * 收藏帖子
     */
    Boolean collectPost(Long postId, Long userId);
    
    /**
     * 取消收藏帖子
     */
    Boolean uncollectPost(Long postId, Long userId);
    
    /**
     * 检查用户是否已收藏帖子
     */
    Boolean isUserCollected(Long postId, Long userId);
    
    /**
     * 获取帖子收藏数
     */
    Integer getCollectCount(Long postId);
    
    /**
     * 获取用户收藏的帖子列表
     */
    Page<PostVO> getUserCollects(Long userId, Integer page, Integer size);
} 