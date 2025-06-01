package com.gongda.gongdacircle.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gongda.gongdacircle.common.Result;
import com.gongda.gongdacircle.dto.PostDTO;
import com.gongda.gongdacircle.service.PostService;
import com.gongda.gongdacircle.utils.SecurityUtil;
import com.gongda.gongdacircle.vo.PostVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 帖子控制器
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;
    
    /**
     * 发布帖子
     */
    @PostMapping("/publish")
    public Result<Boolean> publishPost(@Validated @RequestBody PostDTO postDTO) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            boolean result = postService.publishPost(postDTO, userId);
            return result ? Result.success("发布成功") : Result.error("发布失败");
        } catch (Exception e) {
            log.error("发布帖子异常：", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取帖子详情
     */
    @GetMapping("/{id}")
    public Result<PostVO> getPostById(@PathVariable Long id) {
        try {
            PostVO postVO = postService.getPostById(id);
            return postVO != null ? Result.success(postVO) : Result.error("帖子不存在");
        } catch (Exception e) {
            log.error("获取帖子详情异常：", e);
            return Result.error("获取帖子详情失败");
        }
    }
    
    /**
     * 分页查询帖子列表
     */
    @GetMapping("/list")
    public Result<Page<PostVO>> getPostList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer type) {
        try {
            Page<PostVO> result = postService.getPostList(page, size, keyword, userId, type);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询帖子列表异常：", e);
            return Result.error("查询帖子列表失败");
        }
    }
    
    /**
     * 获取当前用户的帖子列表
     */
    @GetMapping("/my")
    public Result<Page<PostVO>> getMyPostList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer type) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            Page<PostVO> result = postService.getPostList(page, size, keyword, userId, type);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询我的帖子列表异常：", e);
            return Result.error("查询我的帖子列表失败");
        }
    }
    
    /**
     * 更新帖子
     */
    @PutMapping("/{id}")
    public Result<Boolean> updatePost(@PathVariable Long id, @Validated @RequestBody PostDTO postDTO) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            boolean result = postService.updatePost(id, postDTO, userId);
            return result ? Result.success("更新成功") : Result.error("更新失败");
        } catch (Exception e) {
            log.error("更新帖子异常：", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除帖子
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deletePost(@PathVariable Long id) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            boolean result = postService.deletePost(id, userId);
            return result ? Result.success("删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            log.error("删除帖子异常：", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 点赞帖子
     */
    @PostMapping("/{id}/like")
    public Result<Boolean> likePost(@PathVariable Long id) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            boolean result = postService.likePost(id, userId);
            return result ? Result.success("点赞成功") : Result.error("点赞失败");
        } catch (Exception e) {
            log.error("点赞帖子异常：", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 收藏帖子
     */
    @PostMapping("/{id}/collect")
    public Result<Boolean> collectPost(@PathVariable Long id) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            boolean result = postService.collectPost(id, userId);
            return result ? Result.success("收藏成功") : Result.error("收藏失败");
        } catch (Exception e) {
            log.error("收藏帖子异常：", e);
            return Result.error(e.getMessage());
        }
    }
} 