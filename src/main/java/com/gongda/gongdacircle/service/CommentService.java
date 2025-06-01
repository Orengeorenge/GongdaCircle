package com.gongda.gongdacircle.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gongda.gongdacircle.dto.CommentDTO;
import com.gongda.gongdacircle.vo.CommentVO;

/**
 * 评论服务接口
 */
public interface CommentService {
    
    /**
     * 发布评论
     */
    Long publishComment(CommentDTO commentDTO, Long userId);
    
    /**
     * 分页查询帖子评论列表
     */
    Page<CommentVO> getCommentPage(Long postId, Integer page, Integer size, Long currentUserId);
    
    /**
     * 删除评论
     */
    Boolean deleteComment(Long commentId, Long userId);
    
    /**
     * 点赞评论
     */
    Boolean likeComment(Long commentId, Long userId);
    
    /**
     * 取消点赞评论
     */
    Boolean unlikeComment(Long commentId, Long userId);
} 