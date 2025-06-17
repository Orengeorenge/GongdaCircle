package com.gongda.gongdacircle.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gongda.gongdacircle.common.Result;
import com.gongda.gongdacircle.dto.FriendDTO;
import com.gongda.gongdacircle.service.FriendService;
import com.gongda.gongdacircle.utils.SecurityUtil;
import com.gongda.gongdacircle.vo.FriendVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 好友关系控制器
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {
    
    private final FriendService friendService;
    
    /**
     * 申请添加好友
     */
    @PostMapping("/apply")
    public Result<Boolean> applyFriend(@Validated @RequestBody FriendDTO friendDTO) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            boolean result = friendService.applyFriend(friendDTO, userId);
            if (result) {
                return Result.success("好友申请已发送");
            } else {
                return Result.error("申请失败");
            }
        } catch (Exception e) {
            log.error("申请添加好友异常：", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 同意好友申请
     */
    @PostMapping("/{friendshipId}/approve")
    public Result<Boolean> approveFriendRequest(@PathVariable Long friendshipId) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            boolean result = friendService.approveFriendRequest(friendshipId, userId);
            if (result) {
                return Result.success("已同意好友申请");
            } else {
                return Result.error("处理失败");
            }
        } catch (Exception e) {
            log.error("同意好友申请异常：", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 拒绝好友申请
     */
    @PostMapping("/{friendshipId}/reject")
    public Result<Boolean> rejectFriendRequest(@PathVariable Long friendshipId) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            boolean result = friendService.rejectFriendRequest(friendshipId, userId);
            if (result) {
                return Result.success("已拒绝好友申请");
            } else {
                return Result.error("处理失败");
            }
        } catch (Exception e) {
            log.error("拒绝好友申请异常：", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除好友
     */
    @DeleteMapping("/{friendshipId}")
    public Result<Boolean> deleteFriend(@PathVariable Long friendshipId) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            boolean result = friendService.deleteFriend(friendshipId, userId);
            if (result) {
                return Result.success("已删除好友");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除好友异常：", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取好友列表
     */
    @GetMapping("/list")
    public Result<Page<FriendVO>> getFriendList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            Page<FriendVO> result = friendService.getFriendList(userId, page, size, keyword);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取好友列表异常：", e);
            return Result.error("获取好友列表失败");
        }
    }
    
    /**
     * 获取好友申请列表（收到的申请）
     */
    @GetMapping("/requests")
    public Result<Page<FriendVO>> getFriendRequests(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            Page<FriendVO> result = friendService.getFriendRequests(userId, page, size);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取好友申请列表异常：", e);
            return Result.error("获取好友申请列表失败");
        }
    }
    
    /**
     * 获取我发送的好友申请列表
     */
    @GetMapping("/sent")
    public Result<Page<FriendVO>> getMySentRequests(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            Page<FriendVO> result = friendService.getMySentRequests(userId, page, size);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取我的申请列表异常：", e);
            return Result.error("获取我的申请列表失败");
        }
    }
    
    /**
     * 检查是否为好友
     */
    @GetMapping("/check/{targetUserId}")
    public Result<Boolean> checkFriendship(@PathVariable Long targetUserId) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            boolean isFriend = friendService.isFriend(userId, targetUserId);
            return Result.success(isFriend);
        } catch (Exception e) {
            log.error("检查好友关系异常：", e);
            return Result.error("检查好友关系失败");
        }
    }
    
    /**
     * 检查是否已申请好友
     */
    @GetMapping("/check-applied/{targetUserId}")
    public Result<Boolean> checkApplied(@PathVariable Long targetUserId) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            boolean hasApplied = friendService.hasApplied(userId, targetUserId);
            return Result.success(hasApplied);
        } catch (Exception e) {
            log.error("检查申请状态异常：", e);
            return Result.error("检查申请状态失败");
        }
    }
    
    /**
     * 获取好友关系详情
     */
    @GetMapping("/{friendshipId}")
    public Result<FriendVO> getFriendshipById(@PathVariable Long friendshipId) {
        try {
            Long userId = SecurityUtil.getCurrentUserId();
            if (userId == null) {
                return Result.error(401, "请先登录");
            }
            
            FriendVO friendVO = friendService.getFriendshipById(friendshipId, userId);
            if (friendVO != null) {
                return Result.success(friendVO);
            } else {
                return Result.error("好友关系不存在");
            }
        } catch (Exception e) {
            log.error("获取好友关系详情异常：", e);
            return Result.error(e.getMessage());
        }
    }
} 