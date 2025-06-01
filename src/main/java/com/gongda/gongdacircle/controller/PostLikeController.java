package com.gongda.gongdacircle.controller;

import com.gongda.gongdacircle.common.Result;
import com.gongda.gongdacircle.service.PostLikeService;
import com.gongda.gongdacircle.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 帖子点赞控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostLikeController {
    
    private final PostLikeService postLikeService;
    
    /**
     * 点赞帖子
     */
    @PostMapping("/{postId}/like")
    public Result<Boolean> likePost(@PathVariable Long postId, HttpServletRequest request) {
        try {
            Long userId = JwtUtil.getUserIdFromRequest(request);
            Boolean result = postLikeService.likePost(postId, userId);
            if (result) {
                return Result.success("点赞成功", true);
            } else {
                return Result.error("您已经点赞过了");
            }
        } catch (Exception e) {
            log.error("点赞帖子失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 取消点赞帖子
     */
    @DeleteMapping("/{postId}/like")
    public Result<Boolean> unlikePost(@PathVariable Long postId, HttpServletRequest request) {
        try {
            Long userId = JwtUtil.getUserIdFromRequest(request);
            Boolean result = postLikeService.unlikePost(postId, userId);
            if (result) {
                return Result.success(true, "取消点赞成功");
            } else {
                return Result.error("您还未点赞过");
            }
        } catch (Exception e) {
            log.error("取消点赞帖子失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 检查用户是否已点赞帖子
     */
    @GetMapping("/{postId}/like/status")
    public Result<Boolean> checkLikeStatus(@PathVariable Long postId, HttpServletRequest request) {
        try {
            Long userId = JwtUtil.getUserIdFromRequest(request);
            Boolean isLiked = postLikeService.isUserLiked(postId, userId);
            return Result.success(isLiked);
        } catch (Exception e) {
            log.error("检查点赞状态失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取帖子点赞数
     */
    @GetMapping("/{postId}/like/count")
    public Result<Integer> getLikeCount(@PathVariable Long postId) {
        try {
            Integer count = postLikeService.getLikeCount(postId);
            return Result.success(count);
        } catch (Exception e) {
            log.error("获取点赞数失败", e);
            return Result.error(e.getMessage());
        }
    }
} 