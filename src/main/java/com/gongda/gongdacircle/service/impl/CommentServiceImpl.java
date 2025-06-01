package com.gongda.gongdacircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongda.gongdacircle.dto.CommentDTO;
import com.gongda.gongdacircle.entity.Comment;
import com.gongda.gongdacircle.entity.Post;
import com.gongda.gongdacircle.mapper.CommentMapper;
import com.gongda.gongdacircle.mapper.PostMapper;
import com.gongda.gongdacircle.service.CommentService;
import com.gongda.gongdacircle.utils.IdGenerator;
import com.gongda.gongdacircle.vo.CommentVO;
import com.gongda.gongdacircle.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    
    private final CommentMapper commentMapper;
    private final PostMapper postMapper;
    
    @Override
    @Transactional
    public Long publishComment(CommentDTO commentDTO, Long userId) {
        // 检查帖子是否存在
        Post post = postMapper.selectById(commentDTO.getPostId());
        if (post == null || post.getDeleted() == 1) {
            throw new RuntimeException("帖子不存在");
        }
        
        // 如果是回复评论，检查父评论是否存在
        if (commentDTO.getParentId() != null) {
            Comment parentComment = commentMapper.selectById(commentDTO.getParentId());
            if (parentComment == null || parentComment.getDeleted() == 1) {
                throw new RuntimeException("父评论不存在");
            }
        }
        
        // 创建评论
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setId(IdGenerator.generateId());
        comment.setUserId(userId);
        comment.setLikeCount(0);
        comment.setStatus(1); // 已发布
        comment.setCreateBy(userId);
        comment.setUpdateBy(userId);
        
        // 保存评论
        commentMapper.insert(comment);
        
        // 更新帖子评论数
        postMapper.increaseCommentCount(commentDTO.getPostId());
        
        log.info("用户{}发布评论成功，评论ID：{}", userId, comment.getId());
        return comment.getId();
    }
    
    @Override
    public Page<CommentVO> getCommentPage(Long postId, Integer page, Integer size, Long currentUserId) {
        Page<CommentVO> commentPage = new Page<>(page, size);
        
        // 查询评论列表（只查询顶级评论，不包括回复）
        Page<CommentVO> result = commentMapper.selectCommentPage(commentPage, postId, currentUserId);
        
        // 为每个评论查询回复列表
        for (CommentVO comment : result.getRecords()) {
            List<CommentVO> replies = commentMapper.selectRepliesByParentId(comment.getId(), currentUserId);
            comment.setReplies(replies);
            
            // 处理图片URL
            if (StringUtils.hasText(comment.getContent())) {
                // 这里可以添加图片URL处理逻辑
            }
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public Boolean deleteComment(Long commentId, Long userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null || comment.getDeleted() == 1) {
            throw new RuntimeException("评论不存在");
        }
        
        // 检查权限：只有评论作者可以删除
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("无权限删除此评论");
        }
        
        // 逻辑删除评论
        comment.setDeleted(1);
        comment.setUpdateBy(userId);
        commentMapper.updateById(comment);
        
        // 更新帖子评论数
        postMapper.decreaseCommentCount(comment.getPostId());
        
        log.info("用户{}删除评论成功，评论ID：{}", userId, commentId);
        return true;
    }
    
    @Override
    public Boolean likeComment(Long commentId, Long userId) {
        // TODO: 实现评论点赞功能
        // 这里需要创建评论点赞表和相关逻辑
        log.info("用户{}点赞评论，评论ID：{}", userId, commentId);
        return true;
    }
    
    @Override
    public Boolean unlikeComment(Long commentId, Long userId) {
        // TODO: 实现取消评论点赞功能
        log.info("用户{}取消点赞评论，评论ID：{}", userId, commentId);
        return true;
    }
} 