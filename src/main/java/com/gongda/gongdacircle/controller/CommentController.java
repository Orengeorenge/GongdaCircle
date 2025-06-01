package com.gongda.gongdacircle.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gongda.gongdacircle.common.Result;
import com.gongda.gongdacircle.dto.CommentDTO;
import com.gongda.gongdacircle.service.CommentService;
import com.gongda.gongdacircle.utils.JwtUtil;
import com.gongda.gongdacircle.vo.CommentVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 评论控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Validated
public class CommentController {
    
    private final CommentService commentService;
    
    /**
     * 发布评论
     */
    @PostMapping("/publish")
    public Result<Long> publishComment(@Valid @RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        try {
            Long userId = JwtUtil.getUserIdFromRequest(request);
            Long commentId = commentService.publishComment(commentDTO, userId);
            return Result.success("评论发布成功", commentId);
        } catch (Exception e) {
            log.error("发布评论失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 分页查询帖子评论列表
     */
    @GetMapping("/list")
    public Result<Page<CommentVO>> getCommentList(
            @RequestParam Long postId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        try {
            Long currentUserId = JwtUtil.getUserIdFromRequest(request);
            Page<CommentVO> commentPage = commentService.getCommentPage(postId, page, size, currentUserId);
            return Result.success(commentPage);
        } catch (Exception e) {
            log.error("查询评论列表失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/{commentId}")
    public Result<Boolean> deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        try {
            Long userId = JwtUtil.getUserIdFromRequest(request);
            Boolean result = commentService.deleteComment(commentId, userId);
            return Result.success("评论删除成功", result);
        } catch (Exception e) {
            log.error("删除评论失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 点赞评论
     */
    @PostMapping("/{commentId}/like")
    public Result<Boolean> likeComment(@PathVariable Long commentId, HttpServletRequest request) {
        try {
            Long userId = JwtUtil.getUserIdFromRequest(request);
            Boolean result = commentService.likeComment(commentId, userId);
            return Result.success("点赞成功", result);
        } catch (Exception e) {
            log.error("点赞评论失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 取消点赞评论
     */
    @DeleteMapping("/{commentId}/like")
    public Result<Boolean> unlikeComment(@PathVariable Long commentId, HttpServletRequest request) {
        try {
            Long userId = JwtUtil.getUserIdFromRequest(request);
            Boolean result = commentService.unlikeComment(commentId, userId);
            return Result.success("取消点赞成功", result);
        } catch (Exception e) {
            log.error("取消点赞评论失败", e);
            return Result.error(e.getMessage());
        }
    }
} 