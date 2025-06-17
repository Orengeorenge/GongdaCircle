package com.gongda.gongdacircle.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gongda.gongdacircle.common.Result;
import com.gongda.gongdacircle.service.PostCollectService;
import com.gongda.gongdacircle.utils.JwtUtil;
import com.gongda.gongdacircle.vo.PostVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 帖子收藏控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostCollectController {
    
    private final PostCollectService postCollectService;
    
    /**
     * 收藏帖子
     */
    @PostMapping("/{postId}/collect")
    public Result<Boolean> collectPost(@PathVariable Long postId, HttpServletRequest request) {
        try {
            Long userId = JwtUtil.getUserIdFromRequest(request);
            Boolean result = postCollectService.collectPost(postId, userId);
            if (result) {
                return Result.success("收藏成功", true);
            } else {
                return Result.error("您已经收藏过了");
            }
        } catch (Exception e) {
            log.error("收藏帖子失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 取消收藏帖子
     */
    @DeleteMapping("/{postId}/collect")
    public Result<Boolean> uncollectPost(@PathVariable Long postId, HttpServletRequest request) {
        try {
            Long userId = JwtUtil.getUserIdFromRequest(request);
            Boolean result = postCollectService.uncollectPost(postId, userId);
            if (result) {
                return Result.success("取消收藏成功", true);
            } else {
                return Result.error("您还未收藏过");
            }
        } catch (Exception e) {
            log.error("取消收藏帖子失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 检查用户是否已收藏帖子
     */
    @GetMapping("/{postId}/collect/status")
    public Result<Boolean> checkCollectStatus(@PathVariable Long postId, HttpServletRequest request) {
        try {
            Long userId = JwtUtil.getUserIdFromRequest(request);
            Boolean isCollected = postCollectService.isUserCollected(postId, userId);
            return Result.success(isCollected);
        } catch (Exception e) {
            log.error("检查收藏状态失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取帖子收藏数
     */
    @GetMapping("/{postId}/collect/count")
    public Result<Integer> getCollectCount(@PathVariable Long postId) {
        try {
            Integer count = postCollectService.getCollectCount(postId);
            return Result.success(count);
        } catch (Exception e) {
            log.error("获取收藏数失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取用户收藏的帖子列表
     */
    @GetMapping("/collect/my")
    public Result<Page<PostVO>> getMyCollects(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        try {
            Long userId = JwtUtil.getUserIdFromRequest(request);
            Page<PostVO> collectPage = postCollectService.getUserCollects(userId, page, size);
            return Result.success(collectPage);
        } catch (Exception e) {
            log.error("获取用户收藏列表失败", e);
            return Result.error(e.getMessage());
        }
    }
} 