package com.gongda.gongdacircle.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gongda.gongdacircle.dto.FriendDTO;
import com.gongda.gongdacircle.entity.Friend;
import com.gongda.gongdacircle.vo.FriendVO;

/**
 * 好友关系服务接口
 *
 * @author GongdaCircle
 * @since 2024-01-01
 */
public interface FriendService extends IService<Friend> {
    
    /**
     * 申请添加好友
     * @param friendDTO 好友申请信息
     * @param userId 申请者用户ID
     * @return 申请结果
     */
    boolean applyFriend(FriendDTO friendDTO, Long userId);
    
    /**
     * 同意好友申请
     * @param friendshipId 好友关系ID
     * @param userId 当前用户ID
     * @return 处理结果
     */
    boolean approveFriendRequest(Long friendshipId, Long userId);
    
    /**
     * 拒绝好友申请
     * @param friendshipId 好友关系ID
     * @param userId 当前用户ID
     * @return 处理结果
     */
    boolean rejectFriendRequest(Long friendshipId, Long userId);
    
    /**
     * 删除好友关系
     * @param friendshipId 好友关系ID
     * @param userId 当前用户ID
     * @return 删除结果
     */
    boolean deleteFriend(Long friendshipId, Long userId);
    
    /**
     * 获取好友列表
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页数量
     * @param keyword 搜索关键词
     * @return 好友列表
     */
    Page<FriendVO> getFriendList(Long userId, Integer page, Integer size, String keyword);
    
    /**
     * 获取好友申请列表（收到的申请）
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页数量
     * @return 好友申请列表
     */
    Page<FriendVO> getFriendRequests(Long userId, Integer page, Integer size);
    
    /**
     * 获取我发送的好友申请列表
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页数量
     * @return 发送的好友申请列表
     */
    Page<FriendVO> getMySentRequests(Long userId, Integer page, Integer size);
    
    /**
     * 检查两个用户是否为好友
     * @param userId1 用户1ID
     * @param userId2 用户2ID
     * @return 是否为好友
     */
    boolean isFriend(Long userId1, Long userId2);
    
    /**
     * 检查是否已发送好友申请
     * @param userId 申请者ID
     * @param friendId 被申请者ID
     * @return 是否已申请
     */
    boolean hasApplied(Long userId, Long friendId);
    
    /**
     * 根据ID获取好友关系详情
     * @param friendshipId 好友关系ID
     * @param userId 当前用户ID
     * @return 好友关系详情
     */
    FriendVO getFriendshipById(Long friendshipId, Long userId);
} 